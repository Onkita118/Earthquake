package com.example.earthquake;

    public class earthQuake {
        private String mplace;
        private double mScale;
        /** Time of the earthquake */
        private long mTimeInMilliseconds;

        /** Website URL of the earthquake */
        private String mUrl;

        public earthQuake(String place,double scale, long timeInMilliseconds, String url)
        {
            mplace=place;
            mScale=scale;
            mTimeInMilliseconds = timeInMilliseconds;
            mUrl = url;        }

        public String getMplace(){ return mplace;}
        public double getmScale(){return mScale;}
        public long getTimeInMilliseconds() {
            return mTimeInMilliseconds;
        }

        /**
         * Returns the website URL to find more information about the earthquake.
         */
        public String getUrl() {
            return mUrl;
        }    }

