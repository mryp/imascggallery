package net.poringsoft.imascggallery;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * ナビゲーション表示用アダプター
 */
public class NavigationListAdapter extends BaseSectionAdapter<NaviSectionHeaderData, NaviSectionRowData> {
    //プロパティ
    //-----------------------------------------------------------
    public List<List<NaviSectionRowData>> getRowList()
    {
        return this.rowList;
    }

    //メソッド
    //-----------------------------------------------------------
    /**
     * コンストラクタ
     * @param context コンテキスト
     * @param sectionList セッションリスト
     * @param rowList 行リスト
     */
    public NavigationListAdapter(Context context, List<NaviSectionHeaderData> sectionList,
                                     List<List<NaviSectionRowData>> rowList) {
        super(context, sectionList, rowList);
    }

    /**
     * ヘッダー行
     */
    @Override
    public View viewForHeaderInSection(View convertView, int section)
    {
        ListHeaderViewHolder holder = null;
        Resources resources = context.getResources();
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.main_navi_header, null);
            if (convertView != null) {
                holder = new ListHeaderViewHolder();
                holder.titleTxt = (TextView) convertView.findViewById(R.id.titleTxt);
                convertView.setTag(holder);

                RelativeLayout layout = (RelativeLayout)convertView.findViewById(R.id.titleLayout);
                layout.setBackgroundColor(resources.getColor(R.color.main_background));
            }
        }
        else
        {
            holder = (ListHeaderViewHolder)convertView.getTag();
        }

        if (holder != null) {
            NaviSectionHeaderData headerData = sectionList.get(section);
            holder.titleTxt.setText(headerData.getTitle());
            holder.titleTxt.setTextColor(resources.getColor(R.color.main_primary_dark));
        }
        return convertView;
    }

    /**
     * データ行
     */
    @Override
    public View cellForRowAtIndexPath(int position, View convertView, ViewGroup parent, IndexPath indexPath) {
        ListRowViewHolder holder = null;
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.navi_data_row, null);
            if (convertView != null) {
                holder = new ListRowViewHolder();
                holder.labelTxt = (TextView) convertView.findViewById(R.id.labelTxt);
                holder.valueTxt = (TextView) convertView.findViewById(R.id.valueTxt);
                convertView.setTag(holder);
            }
        }
        else
        {
            holder = (ListRowViewHolder)convertView.getTag();
        }

        if (holder != null) {
            NaviSectionRowData rowData = rowList.get(indexPath.section).get(indexPath.row);

            LabelItem item = new LabelItem(holder.labelTxt, holder.valueTxt, rowData);
            holder.labelTxt.setText(rowData.getLabel());
            holder.valueTxt.setText(rowData.getValue());
        }

        ListView listView = (ListView)parent;
        Resources resources = context.getResources();
        if (convertView != null && holder != null) {
            if (listView.getCheckedItemPosition() == position) {
                //選択時の背景色
                convertView.setBackgroundColor(resources.getColor(R.color.main_select_background));
                holder.labelTxt.setTextColor(resources.getColor(R.color.main_select_text));
                holder.valueTxt.setTextColor(resources.getColor(R.color.main_select_text));
            } else {
                //通常時の背景色
                convertView.setBackgroundResource(R.drawable.navi_selector);
                holder.labelTxt.setTextColor(resources.getColor(R.color.main_text_dark));
                holder.valueTxt.setTextColor(resources.getColor(R.color.main_text_dark));
            }
        }
        return convertView;
    }

    /**
     * ラベルクラス
     */
    private class LabelItem
    {
        private TextView m_labelTextView;
        private TextView m_valueTextView;
        private NaviSectionRowData m_rowData;

        public TextView getLabelTextView() {
            return m_labelTextView;
        }

        public TextView getValueTextView() {
            return m_valueTextView;
        }

        public NaviSectionRowData getRowData() {
            return m_rowData;
        }

        public LabelItem(TextView labelView, TextView valueView, NaviSectionRowData rowData) {
            m_labelTextView = labelView;
            m_valueTextView = valueView;
            m_rowData = rowData;
        }
    }

    /**
     * ヘッダーのアイテム
     */
    static class ListHeaderViewHolder {
        TextView titleTxt;
    }

    /**
     * データのアイテム
     */
    static class ListRowViewHolder {
        TextView labelTxt;
        TextView valueTxt;
    }
}
