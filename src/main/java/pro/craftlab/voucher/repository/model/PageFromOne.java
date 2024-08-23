package pro.craftlab.voucher.repository.model;

import java.util.Objects;

public final class PageFromOne {
  public static final int MIN_PAGE = 1;
  private final Integer value;

  public PageFromOne(Integer value) {
    if (value == null) {
      value = MIN_PAGE;
    } else {
      if (value < MIN_PAGE) {
        throw new RuntimeException("page must be >= 0");
      }
    }
    this.value = value;
  }

  public Integer value() {
    return value - 1;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    var that = (PageFromOne) obj;
    return Objects.equals(this.value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

  @Override
  public String toString() {
    return "PageFromOne[" + "value=" + value + ']';
  }
}
