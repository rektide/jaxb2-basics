package org.jvnet.jaxb2_commons.lang.builder;

import java.util.Collection;

import org.jvnet.jaxb2_commons.lang.MergeFrom;
import org.jvnet.jaxb2_commons.lang.Mergeable;

public class MergeBuilder {

	@SuppressWarnings("unchecked")
	public <T> T merge(Object left, Object right, String property, T leftValue,
			T rightValue) {
		if (leftValue instanceof Collection<?>
				&& rightValue instanceof Collection<?>) {
			final Collection<?> leftCollection = (Collection<?>) leftValue;
			return !leftCollection.isEmpty() ? leftValue : rightValue;
		} else {
			if (leftValue == null) {
				return rightValue;
			} else if (rightValue == null) {
				return leftValue;
			} else {
				if (leftValue instanceof MergeFrom) {
					return (T) ((MergeFrom) leftValue).mergeFrom(leftValue,
							rightValue, this);
				} else if (leftValue instanceof Mergeable) {
					return (T) ((Mergeable) leftValue).mergeFrom(leftValue,
							rightValue);
				} else {
					return leftValue;
				}
			}
		}
	}

	public long merge(Object left, Object right, String property,
			long leftValue, long rightValue) {
		return leftValue != 0 ? leftValue : rightValue;
	}

	public int merge(Object left, Object right, String property, int leftValue,
			int rightValue) {
		return leftValue != 0 ? leftValue : rightValue;
	}

	public short merge(Object left, Object right, String property,
			short leftValue, short rightValue) {
		return leftValue != 0 ? leftValue : rightValue;
	}

	public char merge(Object left, Object right, String property,
			char leftValue, char rightValue) {
		return leftValue != 0 ? leftValue : rightValue;
	}

	public byte merge(Object left, Object right, String property,
			byte leftValue, byte rightValue) {
		return leftValue != 0 ? leftValue : rightValue;
	}

	public double merge(Object left, Object right, String property,
			double leftValue, double rightValue) {
		return leftValue != 0 ? leftValue : rightValue;
	}

	public float merge(Object left, Object right, String property,
			float leftValue, float rightValue) {
		return leftValue != 0 ? leftValue : rightValue;
	}

	public boolean merge(Object left, Object right, String property,
			boolean leftValue, boolean rightValue) {
		return leftValue ? leftValue : rightValue;
	}

	public <T> T[] merge(Object left, Object right, String property,
			T[] leftValue, T[] rightValue) {
		if (leftValue != null) {
			if (rightValue != null) {
				return leftValue.length > 0 ? leftValue : rightValue;

			} else {
				return leftValue;
			}
		} else {
			return rightValue;
		}
	}

	public long[] merge(Object left, Object right, String property,
			long[] leftValue, long[] rightValue) {
		if (leftValue != null) {
			if (rightValue != null) {
				return leftValue.length > 0 ? leftValue : rightValue;

			} else {
				return leftValue;
			}
		} else {
			return rightValue;
		}
	}

	public int[] merge(Object left, Object right, String property,
			int[] leftValue, int[] rightValue) {
		if (leftValue != null) {
			if (rightValue != null) {
				return leftValue.length > 0 ? leftValue : rightValue;

			} else {
				return leftValue;
			}
		} else {
			return rightValue;
		}
	}

	public short[] merge(Object left, Object right, String property,
			short[] leftValue, short[] rightValue) {
		if (leftValue != null) {
			if (rightValue != null) {
				return leftValue.length > 0 ? leftValue : rightValue;

			} else {
				return leftValue;
			}
		} else {
			return rightValue;
		}
	}

	public char[] merge(Object left, Object right, String property,
			char[] leftValue, char[] rightValue) {
		if (leftValue != null) {
			if (rightValue != null) {
				return leftValue.length > 0 ? leftValue : rightValue;

			} else {
				return leftValue;
			}
		} else {
			return rightValue;
		}
	}

	public byte[] merge(Object left, Object right, String property,
			byte[] leftValue, byte[] rightValue) {
		if (leftValue != null) {
			if (rightValue != null) {
				return leftValue.length > 0 ? leftValue : rightValue;

			} else {
				return leftValue;
			}
		} else {
			return rightValue;
		}
	}

	public double[] merge(Object left, Object right, String property,
			double[] leftValue, double[] rightValue) {
		if (leftValue != null) {
			if (rightValue != null) {
				return leftValue.length > 0 ? leftValue : rightValue;

			} else {
				return leftValue;
			}
		} else {
			return rightValue;
		}
	}

	public float[] merge(Object left, Object right, String property,
			float[] leftValue, float[] rightValue) {
		if (leftValue != null) {
			if (rightValue != null) {
				return leftValue.length > 0 ? leftValue : rightValue;

			} else {
				return leftValue;
			}
		} else {
			return rightValue;
		}
	}

	public boolean[] merge(Object left, Object right, String property,
			boolean[] leftValue, boolean[] rightValue) {
		if (leftValue != null) {
			if (rightValue != null) {
				return leftValue.length > 0 ? leftValue : rightValue;

			} else {
				return leftValue;
			}
		} else {
			return rightValue;
		}
	}
}
