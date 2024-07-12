package pro.craftlab.voucher.file;

import pro.craftlab.voucher.PojaGenerated;

@PojaGenerated
public record FileHash(FileHashAlgorithm algorithm, String value) {}
