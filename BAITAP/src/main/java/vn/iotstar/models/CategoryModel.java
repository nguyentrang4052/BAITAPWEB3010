package vn.iotstar.models;

import java.io.Serializable;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryModel implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5892006357839137141L;
	private Long id;
	@NotEmpty(message="không được bỏ trống")
	private String name;
	private String images;
	private int status;
	
	private Boolean isEdit=false;
}
