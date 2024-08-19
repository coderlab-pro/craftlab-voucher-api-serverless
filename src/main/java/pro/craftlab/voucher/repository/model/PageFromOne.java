package pro.craftlab.voucher.repository.model;

public record PageFromOne(Integer value) {
  public static final int MIN_PAGE = 1;

  public PageFromOne {
    if (value == null) {
      value = MIN_PAGE;
    } else {
      if (value < MIN_PAGE) {
        throw new RuntimeException("page must be >=1");
      }
    }
  }
}
