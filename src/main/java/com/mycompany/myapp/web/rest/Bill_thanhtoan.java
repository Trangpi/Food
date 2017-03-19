package com.mycompany.myapp.web.rest;

/**
 * Created by TrangPi on 12/03/2017.
 */
public class Bill_thanhtoan {

        private  double thanhtoan;
        private  double thanhtoan_sale;
        private  double Ship ;
        private  double Tong ;



        public void setThanhtoan(double thanhtoan){
            this.thanhtoan = thanhtoan;
        }
        public void setThanhtoan_sale(double thanhtoan_sale){
            this.thanhtoan_sale = thanhtoan_sale;
        }
        public void setShip(double Ship){this.Ship = Ship;}
        public void setTong(){
            this.Tong = getThanhtoan() - getThanhtoan_sale() + getShip();
        }



        public void setBill_thanhtoan( double thanhtoan, double thanhtoan_sale,double Ship){

            setThanhtoan(thanhtoan);
            setThanhtoan_sale(thanhtoan_sale);
            setShip(Ship);
            setTong();


        }



        public double getThanhtoan(){ return  this.thanhtoan;}
        public double getThanhtoan_sale(){ return this.thanhtoan_sale; }
        public double getShip(){return this.Ship;}
        public double getTong(){return  this.Tong;}

        @Override
        public String toString(){
            return  "Bill_thanhtoan{" +
                "thanh_toan" + thanhtoan +
                ", thanhtoan_sale'" + thanhtoan_sale + "'" +
                ", tong'" + Tong + "'" +
                '}';
        }




}
