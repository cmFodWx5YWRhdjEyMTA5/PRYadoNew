package com.yado.pryado.pryadonew.ui.widgit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.MPPointF;
import com.yado.pryado.pryadonew.R;
import com.yado.pryado.pryadonew.bean.TempMonitorBean;

import java.util.ArrayList;
import java.util.List;

public class CustomMarkerView extends MarkerView {

    private TextView tvContent1;
    private ArrayList<String> envxVals;

    public CustomMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        // this markerview only displays a textview
        tvContent1 = findViewById(R.id.tvContent1);
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @SuppressLint("SetTextI18n")
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        Chart chart = getChartView();
        StringBuilder builder = new StringBuilder();
        if (envxVals != null && envxVals.size() > 0) {
            builder.append(envxVals.get(e.getXIndex()) + "\n");
        }
        if (chart instanceof LineChart) {
            LineData lineData = ((LineChart) chart).getLineData();
            //获取到图表中的所有曲线
            List<ILineDataSet> dataSetList = lineData.getDataSets();
            builder.append(dataSetList.get(0).getYValForXIndex(e.getXIndex()));
        }
        tvContent1.setText(builder.toString()); // set the entry-value as the display text
    }

    @Override
    public MPPointF getOffset() {
//        tvContent1.setBackgroundResource(R.drawable.chart_popu);
        return new MPPointF(0, -getHeight());
    }

    @Override
    public MPPointF getOffsetRight() {
//        tvContent1.setBackgroundResource(R.drawable.chart_popu_right);
        return new MPPointF(-getWidth(), -getHeight());
    }

    public void setXValue(ArrayList<String> envxVals) {
        this.envxVals = envxVals;
    }
}
