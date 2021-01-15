package com.imjcker.manager.charge.utils;

import com.lemon.common.util.EnumUtils;
/**
 * 枚举常量
 */
public class ConstantEnum {
	/**
	 * 计费分类
	 */
	public static enum BillingType implements EnumUtils.IValueProperty {
		billingBasedTime(1, "时间计费"), billingFees(2, "按条计费");

		private int value;
		private String name;

		BillingType(int value, String name) {
			this.value = value;
			this.name = name;
		}

		@Override
		public int getValue() {
			return value;
		}

		public String getName() {
			return this.name;
		}
	}

	/**
	 * 时间周期
	 */
	public static enum BillingCycle implements EnumUtils.IValueProperty {
		year(1, "包年"), quarter(2, "包季度"), month(3, "包月");

		private int value;
		private String name;

		BillingCycle(int value, String name) {
			this.value = value;
			this.name = name;
		}

		public static int getValueByName(String name) {
			for (BillingCycle p : values()) {
				if (name.equalsIgnoreCase(p.getName())) {
					return p.getValue();
				}
			}
			return 0;
		}

		@Override
		public int getValue() {
			return value;
		}

		public String getName() {
			return this.name;
		}
	}
	/**
	 * 周期内最大调用次数
	 */
	public static enum BillingCycleLimit implements EnumUtils.IValueProperty {
		billingEnquery(-1, "不限");
		private int value;
		private String name;

		BillingCycleLimit(int value, String name) {
			this.value = value;
			this.name = name;
		}

		@Override
		public int getValue() {
			return value;
		}

		public String getName() {
			return this.name;
		}
	}

	/**
	 * 计费模式
	 */
	public static enum BillingMode implements EnumUtils.IValueProperty {
		billingEnquery(1, "查询"), billingCheck(2, "查得");

		private int value;
		private String name;

		BillingMode(int value, String name) {
			this.value = value;
			this.name = name;
		}

		@Override
		public int getValue() {
			return value;
		}

		public String getName() {
			return this.name;
		}
	}
}
