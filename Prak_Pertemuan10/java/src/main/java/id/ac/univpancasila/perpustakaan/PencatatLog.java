package id.ac.univpancasila.perpustakaan;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Logging sederhana: satu baris per kejadian ke berkas log.
 * Format: waktu | jenis exception | data penyebab | pesan
 */
public class PencatatLog {

    private static final DateTimeFormatter FORMAT_WAKTU = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final ZoneId ZONA = ZoneId.of("Asia/Jakarta"); // WIB, konsisten dengan versi PHP

    private final Path berkasLog;

    public PencatatLog(Path berkasLog) {
        this.berkasLog = berkasLog;
    }

    public void catat(String jenis, String dataPenyebab, String pesan) {
        String baris = LocalDateTime.now(ZONA).format(FORMAT_WAKTU)
                + " | " + jenis
                + " | " + dataPenyebab
                + " | " + pesan
                + System.lineSeparator();
        try {
            Path folder = berkasLog.toAbsolutePath().getParent();
            if (folder != null) {
                Files.createDirectories(folder);
            }
            Files.writeString(berkasLog, baris,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            // Gagal mencatat log tidak boleh menjatuhkan program.
            System.err.println("[PERINGATAN] Log tidak dapat ditulis: " + e.getMessage());
        }
    }
}
