package com.yado.pryado.pryadonew.bean;

import java.io.Serializable;
import java.util.List;

public class TypeBean implements Serializable {

    private List<HymannewBean> hymannew;

    public List<HymannewBean> getHymannew() {
        return hymannew;
    }

    public void setHymannew(List<HymannewBean> hymannew) {
        this.hymannew = hymannew;
    }

    public static class HymannewBean implements Serializable {
        /**
         * total : 15
         * total1 : 0
         * total2 : 15
         */

        private String total;
        private String total1;
        private String total2;

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getTotal1() {
            return total1;
        }

        public void setTotal1(String total1) {
            this.total1 = total1;
        }

        public String getTotal2() {
            return total2;
        }

        public void setTotal2(String total2) {
            this.total2 = total2;
        }
    }
}
