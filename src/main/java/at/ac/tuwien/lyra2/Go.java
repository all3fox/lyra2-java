package at.ac.tuwien.lyra2;

public class Go {
    public static void memcpy1(byte[] dst, int offset, int src) {
        dst[offset + 0] = (byte) (src       );
        dst[offset + 1] = (byte) (src >>>  8);
        dst[offset + 2] = (byte) (src >>> 16);
        dst[offset + 3] = (byte) (src >>> 24);
    }

    public static byte[] pack_bytes(long[] longs) {
        byte[] bytes = new byte[8 * longs.length];

        for (int i = 0; i != longs.length; ++i) {
            for (int j = 0; j != 8; ++j) {
                bytes[8 * i + j] = (byte) (longs[i] >>> (56 - j * 8));
            }
        }

        return bytes;
    }

    public static long[] pack_longs(byte[] bytes) {
        int div = bytes.length / 8;
        int mod = bytes.length % 8;

        long[] longs = new long[div + (mod == 0 ? 0 : 1)];

        for (int i = 0; i != div; ++i) {
            long l = 0L;

            for (int j = 0; j != 7; ++j, l <<= 8) {
                // Upcasting a negative value gives a negative value
                // So, mask the result of an upcast to last byte only
                l |= (bytes[i * 8 + j] & 0x00000000000000FFL);

            } l |= bytes[i * 8 + 7] & 0x00000000000000FFL;

            longs[i] = l;
        }

        if (mod != 0) {
            long l = 0;

            for (int i = 0; i != mod - 1; ++i) {
                l |= bytes[div * 8 + i];

                l <<= 8;
            } l |= bytes[div * 8 + mod];

            l <<= (8 * (7 - mod));

            longs[div] = l;
        }

        return longs;
    }

    /**
     * Dump bytes into System.out as hex in an n-by-m grid
     *
     * @param bytes - echo these bytes to console
     * @param n     - try to have n rows
     * @param m     - try to have m cols
     * @param s     - skip s bytes ahead
     */
    public static void dump_bytes(byte[] bytes, int n, int m, int s) {
        int div = n / m;
        int mod = n % m;

        if (s + n > bytes.length) {
            System.out.println("You ask to dump " + (s + n) + " byte(s)");
            System.out.println("Buffer has only " + bytes.length + " byte(s)");
            return;
        }

        for (int i = 0; i != div; ++i) {
            for (int j = 0; j != m; ++j) {
                System.out.printf("%02X ", bytes[s + i * m + j]);
            } System.out.println();
        }

        for (int i = 0; i != mod; ++i) {
            System.out.printf("%02X ", bytes[s + div * m + i]);
        } System.out.println();
    }

    public static void dump_bytes(byte[] bytes, int n) {
        dump_bytes(bytes, n, 16, 0);
    }

    public static void dump_bytes(long[] longs, int n, int m, int s) {
        dump_bytes(pack_bytes(longs), n, m, s);
    }

    public static void dump_bytes(long[] longs, int n) {
        dump_bytes(longs, n, 16, 0);
    }
}