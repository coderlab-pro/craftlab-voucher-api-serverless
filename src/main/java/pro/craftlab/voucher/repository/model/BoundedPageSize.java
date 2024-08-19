package pro.craftlab.voucher.repository.model;

public record BoundedPageSize(Integer value) {
  public static final int MAX_SIZE = 500;

  public BoundedPageSize {
    if (value == null) {
      value = MAX_SIZE - 1;
    } else {
      if (value < 1) {
        throw new RuntimeException("page size must be >=1");
      }
      if (value > MAX_SIZE) {
        throw new RuntimeException("page size must be <" + MAX_SIZE);
      }
    }
  }
}
