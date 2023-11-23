import java.util.Scanner;

public class Fuzzy {

    private static final double SUHU_MIN = 20.0; //Nilai Suhu Minimal
    private static final double SUHU_AVG = 25.0; //Nilai Suhu Tengah
    private static final double SUHU_MAX = 33.0; //Nilai Suhu Maksimal
    private static final double KELEMBABAN_MIN = 20.0; //Nilai Kelembaban Minimal
    private static final double KELEMBABAN_AVG = 30.0; //Nilai Kelembaban Tengah
    private static final double KELEMBABAN_MAX = 40.0; //Nilai Kelembaban Maksimal
    private static final double AIR_MIN = 5.0; //Nilai Kebutuhan Air Sedikit
    private static final double AIR_MAX = 10.0; //Nilai Kebutuhan Air Banyak
    private double rule1; //Rule 1: Jika Suhu Rendah dan Kelembaban Rendah, maka Jumlah Air Sedikit
    private double rule2; //Rule 2: Jika Suhu Rendah dan Kelembaban Sedang, maka Jumlah Air Sedikit
    private double rule3; //Rule 3: Jika Suhu Rendah dan Kelembaban Tinggi, maka Jumlah Air Sedikit
    private double rule4; //Rule 4: Jika Suhu Sedang dan Kelembaban Rendah, maka Jumlah Air Sedikit
    private double rule5; //Rule 5: Jika Suhu Sedang dan Kelembaban Sedang, maka Jumlah Air Banyak
    private double rule6; //Rule 6: Jika Suhu Sedang dan Kelembaban Tinggi, maka Jumlah Air Banyak
    private double rule7; //Rule 7: Jika Suhu Tinggi dan Kelembaban Rendah, maka Jumlah Air Banyak
    private double rule8; //Rule 8: Jika Suhu Tinggi dan Kelembaban Sedang, maka Jumlah Air Banyak
    private double rule9; //Rule 9: Jika Suhu Tinggi dan Kelembaban Tinggi, maka Jumlah Air Banyak

    public static void main(String[] args) {
        Fuzzy fuzzy = new Fuzzy();
        Scanner sc = new Scanner(System.in);

//        Input Suhu
        System.out.print("Masukkan Suhu: ");
        double suhu = sc.nextDouble();

//        Input Kelembaban
        System.out.print("Masukkan Kelembaban: ");
        double kelembaban = sc.nextDouble();

//        Perhitungan Fuzzifikasi Suhu
        double fuzzifikasiSuhuRendah = fuzzy.fuzzifikasi(suhu, "suhu", "rendah");
        double fuzzifikasiSuhuSedang = fuzzy.fuzzifikasi(suhu, "suhu", "sedang");
        double fuzzifikasiSuhuTinggi = fuzzy.fuzzifikasi(suhu, "suhu", "tinggi");

        System.out.println("Fuzzifikasi Suhu:\nRendah: " + fuzzifikasiSuhuRendah
                + "\nSedang: " + fuzzifikasiSuhuSedang + "\nTinggi: " + fuzzifikasiSuhuTinggi);

//        Perhitungan Fuzzifikasi Kelembaban
        double fuzzifikasiKelembabanRendah = fuzzy.fuzzifikasi(kelembaban, "kelembaban", "rendah");
        double fuzzifikasiKelembabanSedang = fuzzy.fuzzifikasi(kelembaban, "kelembaban", "sedang");
        double fuzzifikasiKelembabanTinggi = fuzzy.fuzzifikasi(kelembaban, "kelembaban", "tinggi");

        System.out.println("Fuzzifikasi Kelembaban:\nRendah: " + fuzzifikasiKelembabanRendah
                + "\nSedang: " + fuzzifikasiKelembabanSedang + "\nTinggi: " + fuzzifikasiKelembabanTinggi);

//        Perhitungan Inferensi Rule
        double inferensiRule = fuzzy.inferensiRule(fuzzifikasiSuhuRendah, fuzzifikasiSuhuSedang, fuzzifikasiSuhuTinggi,
                fuzzifikasiKelembabanRendah, fuzzifikasiKelembabanSedang, fuzzifikasiKelembabanTinggi);

        System.out.println("Total Inferensi Rule: " + inferensiRule);

//        Perhitungan Inferensi Air
        double inferensiAir = fuzzy.inferensiAir();

        System.out.println("Total Inferensi Air: " + inferensiAir);

//        Defuzzifikasi
        System.out.println("Hasil Defuzzifikasi: " + fuzzy.defuzzifikasi(inferensiAir, inferensiRule));
    }

    private double fuzzifikasiRendah(double nilai, double nilaiMin, double nilaiAvg) {
//        Nilai Fuzzifikasi Rendah
        if (nilai <= nilaiMin) {
            return 1.0;
        } else if (nilai >= nilaiMin && nilai <= nilaiAvg) {
            return (nilaiAvg - nilai) / (nilaiAvg - nilaiMin);
        } else {
            return 0.0;
        }
    }

    private double fuzzifikasiSedang(double nilai, double nilaiMin, double nilaiAvg, double nilaiMax) {
//        Nilai Fuzzifikasi Sedang
        if (nilai <= nilaiMin) {
            return 0.0;
        } else if (nilai <= nilaiAvg && nilai >= nilaiMin) {
            return (nilai - nilaiMin) / (nilaiAvg - nilaiMin);
        } else if (nilai <= nilaiMax && nilai >= nilaiAvg) {
            return (nilaiMax - nilai) / (nilaiMax - nilaiAvg);
        } else {
            return 1.0;
        }
    }

    private double fuzzifikasiTinggi(double nilai, double nilaiAvg, double nilaiMax) {
//        Nilai Fuzzifikasi Tinggi
        if (nilai <= nilaiAvg) {
            return 0.0;
        } else if (nilai <= nilaiMax && nilai >= nilaiAvg) {
            return (nilai - nilaiAvg) / (nilaiMax - nilaiAvg);
        } else {
            return 1.0;
        }
    }

    private double fuzzifikasi(double nilai, String type, String level) {
//        Perhitungan Fuzzifikasi
        double nilaiMin = type.equals("suhu") ? SUHU_MIN : KELEMBABAN_MIN;
        double nilaiAvg = type.equals("suhu") ? SUHU_AVG : KELEMBABAN_AVG;
        double nilaiMax = type.equals("suhu") ? SUHU_MAX : KELEMBABAN_MAX;

        return switch (level) {
            case "sedang" -> fuzzifikasiSedang(nilai, nilaiMin, nilaiAvg, nilaiMax);
            case "tinggi" -> fuzzifikasiTinggi(nilai, nilaiAvg, nilaiMax);
            default -> fuzzifikasiRendah(nilai, nilaiMin, nilaiAvg);
        };
    }

    private double inferensiRule(double suhuRendah, double suhuSedang, double suhuTinggi, double kelembabanRendah,
                                 double kelembabanSedang, double kelembabanTinggi) {
//        Perhitungan Inferensi Rule
        rule1 = Math.min(suhuRendah, kelembabanRendah);
        rule2 = Math.min(suhuRendah, kelembabanSedang);
        rule3 = Math.min(suhuRendah, kelembabanTinggi);
        rule4 = Math.min(suhuSedang, kelembabanRendah);
        rule5 = Math.min(suhuSedang, kelembabanSedang);
        rule6 = Math.min(suhuSedang, kelembabanTinggi);
        rule7 = Math.min(suhuTinggi, kelembabanRendah);
        rule8 = Math.min(suhuTinggi, kelembabanSedang);
        rule9 = Math.min(suhuTinggi, kelembabanTinggi);

        System.out.println("Nilai Setiap Rule:\nRule 1: " + rule1 + "\nRule 2: " + rule2 + "\nRule 3: " + rule3
                + "\nRule 4: " + rule4 + "\nRule 5: " + rule5 + "\nRule 6: " + rule6 + "\nRule 7: " + rule7
                + "\nRule 8: " + rule8 + "\nRule 9: " + rule9);

        return (rule1 + rule2 + rule3 + rule4 + rule5 + rule6 + rule7 + rule8 + rule9);
    }

    private double inferensiAir() {
//        Perhitungan Inferensi Jumlah Air
        double air1 = rule1 * (AIR_MAX - (rule2 * (AIR_MAX - AIR_MIN)));
        double air2 = rule1 * (AIR_MAX - (rule2 * (AIR_MAX - AIR_MIN)));
        double air3 = rule3 * (AIR_MAX - (rule4 * (AIR_MAX - AIR_MIN)));
        double air4 = rule4 * (AIR_MAX - (rule5 * (AIR_MAX - AIR_MIN)));
        double air5 = rule5 * (AIR_MIN + (rule5 * (AIR_MAX - AIR_MIN)));
        double air6 = rule6 * (AIR_MIN + (rule7 * (AIR_MAX - AIR_MIN)));
        double air7 = rule7 * (AIR_MIN + (rule7 * (AIR_MAX - AIR_MIN)));
        double air8 = rule8 * (AIR_MIN + (rule8 * (AIR_MAX - AIR_MIN)));
        double air9 = rule9 * (AIR_MIN + (rule9 * (AIR_MAX - AIR_MIN)));

        System.out.println("Nilai Jumlah Air:\nAir 1: " + air1 + "\nAir 2: " + air2 + "\nAir 3: " + air3
                + "\nAir 4: " + air4 + "\nAir 5: " + air5 + "\nAir 6: " + air6 + "\nAir 7: " + air7
                + "\nAir 8: " + air8 + "\nAir 9: " + air9);

        return (air1 + air2 + air3 + air4 + air5 + air6 + air7 + air8 + air9);
    }

    private double defuzzifikasi(double inferensiAir, double inferensiRule) {
//        Perhitungan Defuzzifikasi
        return (inferensiAir / inferensiRule);
    }
}