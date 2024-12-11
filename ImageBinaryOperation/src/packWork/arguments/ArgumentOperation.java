package packWork.arguments;

public enum ArgumentOperation {
    AND("AND"), OR("OR"), XOR("XOR");

    private final String name;

    ArgumentOperation(String name) {
        this.name = name;
    }

    public static ArgumentOperation valueOfLabel(String label) {
        for (ArgumentOperation e : values()) {
            if (e.name.equals(label.toUpperCase())) {
                return e;
            }
        }

        return null;
    }
}