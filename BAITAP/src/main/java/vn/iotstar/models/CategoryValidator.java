package vn.iotstar.models;

import java.util.Optional;

import io.micrometer.common.util.StringUtils;

public class CategoryValidator {
	public boolean isValid(CategoryModel category) {
		
		return Optional.ofNullable(category) .filter(t -> !StringUtils.isEmpty(t.getName()))
		.filter(t -> !StringUtils.isEmpty(t.getImages())) .isPresent();
		}
}
