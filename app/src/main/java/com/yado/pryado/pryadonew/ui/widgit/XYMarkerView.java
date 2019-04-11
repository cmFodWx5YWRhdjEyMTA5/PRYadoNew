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

import java.util.List;

import static java.lang.Float.NaN;

public class XYMarkerView extends MarkerView {

    private TextView tvContent1;
    private List<List<String>> xVals;
    private List<TempMonitorBean> monitorBeanList;
    private String station;
    private int type;
    private int dateType;
    private String pName;
    private String temp;

    public XYMarkerView(Context context, int layoutResource) {
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
        String text = "", date1 = "", date2 = "", date3 = "", date4 = "";
        boolean isNodata = false;
//        for (List<String> xVal : xVals) {
//            if (!xVal.get(e.getXIndex()).equals("-")) {
//                isNodata = false;
//                break;
//            } else {
//                isNodata = true;
//            }
//        }
        for (int i = 0; i < xVals.size(); i++) {
            if (e.getXIndex() < xVals.get(i).size()) {
                if (!xVals.get(i).get(e.getXIndex()).equals("-")) {
                    isNodata = false;
                    break;
                } else {
                    isNodata = true;
                }
            }
        }
        if (isNodata) {
            text = "此点没有数据！";
            builder.append(text);
        } else {
            for (int i = 0; i < xVals.size(); i++) {
                if (xVals.get(i).get(e.getXIndex()).length() > 1) {
                    if (dateType == 0) {
                        text = xVals.get(i).get(e.getXIndex()).split(" ")[1];
                    } else {
                        text = xVals.get(i).get(e.getXIndex()).split(" ")[0];
                    }
                    break;
                }
            }
//            if (xVals.get(0).get(e.getXIndex()).length() > 1) {
//                switch (dateType) {
//                    case 0:
//                        text = xVals.get(0).get(e.getXIndex()).split(" ")[1];
//                        break;
//                    case 1:
//                    case 2:
//
//                        text = xVals.get(0).get(e.getXIndex()).split(" ")[0];
//                        date1 = xVals.get(0).get(e.getXIndex()).split(" ")[1];
//                        date2 = xVals.get(1).get(e.getXIndex()).split(" ")[1];
//                        date3 = xVals.get(2).get(e.getXIndex()).split(" ")[1];
//                        date4 = xVals.get(3).get(e.getXIndex()).split(" ")[1];
////                    text = xVals.get(e.getXIndex());
//                        break;
//                    default:
//                        text = xVals.get(0).get(e.getXIndex()).split(" ")[0];
////                    text = xVals.get(e.getXIndex());
//                        break;
//
//                }
//            }


            if (text.contains(":")) {
                String[] split = text.split(":");
                if (split[0].trim().length() == 1) {
                    builder.append("0" + split[0].trim() + ":" + split[1] + ":" + split[2] + "\n");
                } else {
                    builder.append(text + "\n");
                }
            }
            if (text.contains("/")) {
                String[] split1 = text.split("/");
                if (split1[1].trim().length() == 1 && split1[2].trim().length() == 1) {
                    builder.append(split1[0] + "/0" + split1[1].trim() + "/0" + split1[2] + " \n");
                } else if (split1[1].trim().length() == 1 && split1[2].trim().length() != 1) {
                    builder.append(split1[0] + "/0" + split1[1].trim() + "/" + split1[2] + " \n");
                } else if (split1[1].trim().length() != 1 && split1[2].trim().length() == 1) {
                    builder.append(split1[0] + "/" + split1[1].trim() + "/0" + split1[2] + " \n");
                } else {
                    builder.append(split1[0] + "/" + split1[1] + "/" + split1[2] + " \n");
                }
            }
//            if (date1.length() <= 8) {
//                if (date1.contains(":")) {
//                    String[] split = date1.split(":");
//                    if (split[0].trim().length() == 1) {
//                        date1 = "0" + split[0].trim() + ":" + split[1] + ":" + split[2] + "\n";
//                    } else {
//                        date1 = date1 + "\n";
//                    }
//                } else {
//                    date1 = date1 + "\n";
//                }
//            }
//            if (date2.length() <= 8) {
//                if (date2.contains(":")) {
//                    String[] split = date2.split(":");
//                    if (split[0].trim().length() == 1) {
//                        date2 = "0" + split[0].trim() + ":" + split[1] + ":" + split[2] + "\n";
//                    } else {
//                        date2 = date2 + "\n";
//                    }
//                } else {
//                    date2 = date2 + "\n";
//                }
//            }
//            if (date3.length() <= 8) {
//                if (date3.contains(":")) {
//                    String[] split = date3.split(":");
//                    if (split[0].trim().length() == 1) {
//                        date3 = "0" + split[0].trim() + ":" + split[1] + ":" + split[2] + "\n";
//                    } else {
//                        date3 = date3 + "\n";
//                    }
//                } else {
//                    date3 = date3 + "\n";
//                }
//            }
//            if (date4.length() <= 8) {
//                if (date4.contains(":")) {
//                    String[] split = date4.split(":");
//                    if (split[0].trim().length() == 1) {
//                        date4 = "0" + split[0].trim() + ":" + split[1] + ":" + split[2];
//                    }
//                }
//            }
            if (chart instanceof LineChart) {
                LineData lineData = ((LineChart) chart).getLineData();
                //获取到图表中的所有曲线
                List<ILineDataSet> dataSetList = lineData.getDataSets();

                for (int i = 0; i < dataSetList.size(); i++) {
                    String date = "";
                    LineDataSet dataSet = (LineDataSet) dataSetList.get(i);
                    switch (dateType) {
                        case 0:
                            break;
                        case 1:
                        case 2:
                            if (xVals.get(i).get(e.getXIndex()).length() > 1) {
                                date = xVals.get(i).get(e.getXIndex()).split(" ")[1];
                            } else {
                                date = "-";
                            }
                            break;
                        default:
                            break;

                    }
                    if (date.length() <= 8) {
                        if (date.contains(":")) {
                            String[] split = date.split(":");
                            if (split[0].trim().length() == 1) {
                                date = "0" + split[0].trim() + ":" + split[1] + ":" + split[2] + "\n";
                            } else {
                                date = date + "\n";
                            }
                        } else {
                            date = date + "\n";
                        }
                    }
                    float yValForXIndex = dataSet.getYValForXIndex(e.getXIndex());
                    if (Float.isNaN(yValForXIndex) || yValForXIndex == 0) {
                        if (Float.isNaN(yValForXIndex)) {
                            temp = "此点无数据";
                        } else if ( yValForXIndex == 0) {
                            temp = "-";
                        }
                        date = "\n";
                    } else {
                        temp = dataSet.getYValForXIndex(e.getXIndex()) + "";
                    }
                    if (i == dataSetList.size() - 1) {
                        String interval;
                        if (type == 1) {
                            if (i < monitorBeanList.size()) {
                                pName = monitorBeanList.get(i).getpName() + ":";
                            }
                            interval = "          ";
                        } else {
                            if (i < monitorBeanList.size()) {
                                pName = monitorBeanList.get(i).getPosition() + ":";
                            }
                            interval = "      ";
                        }
                        if (dateType > 0) {
                            builder.append(station + pName + temp + interval + date);

                        } else {
                            builder.append(station + pName + temp + "");
                        }
                    } else {
                        if (type == 1) {
                            pName = monitorBeanList.get(i).getpName() + ":";
                        } else {
                            pName = monitorBeanList.get(i).getPosition() + ":";
                        }

                        if (dateType > 0) {
                            builder.append(station + pName + temp + "  " + date);

                        } else {
                            builder.append(station + pName + temp + "" + "\n");

                        }
                    }
//                    switch (i) {
//                        case 0:
//                            if (type == 1) {
//                                pName = monitorBeanList.get(0).getpName() + ":";
//                            } else {
//                                pName = monitorBeanList.get(0).getPosition() + ":";
//                            }
//                            if (dateType > 0) {
//                                builder.append(station + pName + dataSet.getYValForXIndex(e.getXIndex()) + "  " + date1);
//
//                            } else {
//                                builder.append(station + pName + dataSet.getYValForXIndex(e.getXIndex()) + "" + "\n");
//
//                            }
//                            break;
//                        case 1:
//                            if (type == 1) {
//                                pName = monitorBeanList.get(1).getpName() + ":";
//                            } else {
//                                pName = monitorBeanList.get(1).getPosition() + ":";
//                            }
//                            if (dateType > 0) {
//                                builder.append(station + pName + dataSet.getYValForXIndex(e.getXIndex()) + "  " + date2);
//                            } else {
//                                builder.append(station + pName + dataSet.getYValForXIndex(e.getXIndex()) + "" + "\n");
//                            }
//                            break;
//                        case 2:
//                            if (type == 1) {
//                                pName = monitorBeanList.get(2).getpName() + ":";
//                            } else {
//                                pName = monitorBeanList.get(2).getPosition() + ":";
//                            }
//                            if (dateType > 0) {
//                                builder.append(station + pName + dataSet.getYValForXIndex(e.getXIndex()) + "  " + date3);
//                            } else {
//                                builder.append(station + pName + dataSet.getYValForXIndex(e.getXIndex()) + "" + "\n");
//                            }
//                            break;
//                        case 3:
//                            String interval;
//                            if (type == 1) {
//                                pName = monitorBeanList.get(3).getpName() + ":";
//                                interval = "          ";
//                            } else {
//                                pName = monitorBeanList.get(3).getPosition() + ":";
//                                interval = "      ";
//                            }
//                            if (dateType > 0) {
//                                builder.append(station + pName + dataSet.getYValForXIndex(e.getXIndex()) + interval + date4);
//
//                            } else {
//                                builder.append(station + pName + dataSet.getYValForXIndex(e.getXIndex()) + "");
//                            }
//                            break;
//
//                        default:
//                            break;
//                    }

                }
            }
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

    public void setXValue(List<List<String>> xVals) {
        this.xVals = xVals;
    }

    public void setMonitorBeanList(List<TempMonitorBean> monitorBeanList) {
        this.monitorBeanList = monitorBeanList;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setDateType(int dateType) {
        this.dateType = dateType;
    }

    public void reset() {
        if (xVals != null && xVals.size() > 0) {
            xVals.clear();
            xVals = null;
        }
        if (monitorBeanList != null && monitorBeanList.size() > 0) {
            monitorBeanList.clear();
            monitorBeanList = null;
        }
    }
}
