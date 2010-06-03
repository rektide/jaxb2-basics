package org.jvnet.jaxb2_commons.lang;

import org.jvnet.jaxb2_commons.locator.ObjectLocator;

public class DefaultMergeStrategy implements MergeStrategy {

	protected Object mergeInternal(ObjectLocator leftLocator,
			ObjectLocator rightLocator, Object leftValue, Object rightValue) {
		if (leftValue == null) {
			return rightValue;
		} else if (rightValue == null) {
			return leftValue;
		} else {
			if (leftValue instanceof MergeFrom) {
				((MergeFrom) leftValue).mergeFrom(leftLocator, rightLocator,
						rightValue, this);
				return leftValue;
			} else {
				return leftValue;
			}
		}

	}

	public Object merge(ObjectLocator leftLocator, ObjectLocator rightLocator,
			Object left, Object right) {

		if (left == null) {
			return right;
		}
		if (right == null) {
			return left;
		}
		Class<?> lhsClass = left.getClass();
		if (!lhsClass.isArray()) {
			return mergeInternal(leftLocator, rightLocator, left, right);
		} else if (left.getClass() != right.getClass()) {
			// Here when we compare different dimensions, for example: a
			// boolean[][] to a boolean[]
			return false;
		}
		// 'Switch' on type of array, to dispatch to the correct handler
		// This handles multi dimensional arrays of the same depth
		else if (left instanceof long[]) {
			return merge(leftLocator, rightLocator, (long[]) left,
					(long[]) right);
		} else if (left instanceof int[]) {
			return merge(leftLocator, rightLocator, (int[]) left, (int[]) right);
		} else if (left instanceof short[]) {
			return merge(leftLocator, rightLocator, (short[]) left,
					(short[]) right);
		} else if (left instanceof char[]) {
			return merge(leftLocator, rightLocator, (char[]) left,
					(char[]) right);
		} else if (left instanceof byte[]) {
			return merge(leftLocator, rightLocator, (byte[]) left,
					(byte[]) right);
		} else if (left instanceof double[]) {
			return merge(leftLocator, rightLocator, (double[]) left,
					(double[]) right);
		} else if (left instanceof float[]) {
			return merge(leftLocator, rightLocator, (float[]) left,
					(float[]) right);
		} else if (left instanceof boolean[]) {
			return merge(leftLocator, rightLocator, (boolean[]) left,
					(boolean[]) right);
		} else {
			// Not an array of primitives
			return merge(leftLocator, rightLocator, (Object[]) left,
					(Object[]) right);
		}
	}

	public long merge(ObjectLocator leftLocator, ObjectLocator rightLocator,
			long leftValue, long rightValue) {
		return leftValue != 0 ? leftValue : rightValue;
	}

	public int merge(ObjectLocator leftLocator, ObjectLocator rightLocator,
			int leftValue, int rightValue) {
		return leftValue != 0 ? leftValue : rightValue;
	}

	public short merge(ObjectLocator leftLocator, ObjectLocator rightLocator,
			short leftValue, short rightValue) {
		return leftValue != 0 ? leftValue : rightValue;
	}

	public char merge(ObjectLocator leftLocator, ObjectLocator rightLocator,
			char leftValue, char rightValue) {
		return leftValue != 0 ? leftValue : rightValue;
	}

	public byte merge(ObjectLocator leftLocator, ObjectLocator rightLocator,
			byte leftValue, byte rightValue) {
		return leftValue != 0 ? leftValue : rightValue;
	}

	public double merge(ObjectLocator leftLocator, ObjectLocator rightLocator,
			double leftValue, double rightValue) {
		return leftValue != 0 ? leftValue : rightValue;
	}

	public float merge(ObjectLocator leftLocator, ObjectLocator rightLocator,
			float leftValue, float rightValue) {
		return leftValue != 0 ? leftValue : rightValue;
	}

	public boolean merge(ObjectLocator leftLocator, ObjectLocator rightLocator,
			boolean leftValue, boolean rightValue) {
		return leftValue ? leftValue : rightValue;
	}

	public Object[] merge(ObjectLocator leftLocator,
			ObjectLocator rightLocator, Object[] leftValue, Object[] rightValue) {
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

	public long[] merge(ObjectLocator leftLocator, ObjectLocator rightLocator,
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

	public int[] merge(ObjectLocator leftLocator, ObjectLocator rightLocator,
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

	public short[] merge(ObjectLocator leftLocator, ObjectLocator rightLocator,
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

	public char[] merge(ObjectLocator leftLocator, ObjectLocator rightLocator,
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

	public byte[] merge(ObjectLocator leftLocator, ObjectLocator rightLocator,
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

	public double[] merge(ObjectLocator leftLocator,
			ObjectLocator rightLocator, double[] leftValue, double[] rightValue) {
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

	public float[] merge(ObjectLocator leftLocator, ObjectLocator rightLocator,
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

	public boolean[] merge(ObjectLocator leftLocator,
			ObjectLocator rightLocator, boolean[] leftValue,
			boolean[] rightValue) {
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
	
	public static final MergeStrategy INSTANCE = new DefaultMergeStrategy();

}