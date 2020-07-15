package com.carpenter.core.entity.dictionaries;

public enum ArchitectureType {
    RESIDENTIAL,
    INDUSTRIAL,
    ANOTHER;

    public static ArchitectureType of(String dtoArch) {
        if (dtoArch.equals("Budynek mieszkalny")) {
            return ArchitectureType.RESIDENTIAL;
        } else if (dtoArch.equals("Budynek przemysłowy")) {
            return ArchitectureType.INDUSTRIAL;
        } else return ArchitectureType.ANOTHER;
    }
}
