package com.surgedispatch.util;

import java.util.ArrayList;
import java.util.List;

public class GeohashUtils {

    private static final String BASE_32 = "0123456789bcdefghjkmnpqrstuvwxyz";
    private static final int DEFAULT_PRECISION = 5;

    public static String encode(double lat, double lng) {
        return encode(lat, lng, DEFAULT_PRECISION);
    }

    public static String encode(double lat, double lng, int precision) {
        double[] latInterval = {-90.0, 90.0};
        double[] lngInterval = {-180.0, 180.0};

        StringBuilder geohash = new StringBuilder();
        boolean isEven = true;
        int bit = 0;
        int ch = 0;

        while (geohash.length() < precision) {
            double mid;
            if (isEven) {
                mid = (lngInterval[0] + lngInterval[1]) / 2.0;
                if (lng >= mid) {
                    ch |= (1 << (4 - bit));
                    lngInterval[0] = mid;
                } else {
                    lngInterval[1] = mid;
                }
            } else {
                mid = (latInterval[0] + latInterval[1]) / 2.0;
                if (lat >= mid) {
                    ch |= (1 << (4 - bit));
                    latInterval[0] = mid;
                } else {
                    latInterval[1] = mid;
                }
            }

            isEven = !isEven;
            if (bit < 4) {
                bit++;
            } else {
                geohash.append(BASE_32.charAt(ch));
                bit = 0;
                ch = 0;
            }
        }

        return geohash.toString();
    }

    public static List<String> getSearchGeohashes(double lat, double lng) {
        String center = encode(lat, lng, DEFAULT_PRECISION);
        List<String> geohashes = new ArrayList<>();
        geohashes.add(center);

        // Include 8 directional offset steps around center (~0.04 degrees ≈ ~4.4 km)
        double offset = 0.04;
        double[][] directions = {
                {offset, 0}, {-offset, 0}, {0, offset}, {0, -offset},
                {offset, offset}, {offset, -offset}, {-offset, offset}, {-offset, -offset}
        };

        for (double[] dir : directions) {
            String neighbor = encode(lat + dir[0], lng + dir[1], DEFAULT_PRECISION);
            if (!geohashes.contains(neighbor)) {
                geohashes.add(neighbor);
            }
        }

        return geohashes;
    }
}
