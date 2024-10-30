package vn.iotstar.controllers.admin;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.validation.Valid;
import vn.iotstar.entity.Category;
import vn.iotstar.models.CategoryModel;
import vn.iotstar.services.CategoryService;

@Controller
@RequestMapping("admin/category")
public class CategoryController {
	
	@Autowired
	CategoryService cateService;
	
	@RequestMapping("")
	public String all(Model model) {
		List<Category> list = cateService.findAll();
		model.addAttribute("list", list);
		return "admin/category/list";		
	}
	
	@GetMapping("/add")
	public String add(Model model) {
		CategoryModel cateModel = new CategoryModel();
		cateModel.setIsEdit(false);
		model.addAttribute("category",cateModel);
		return "admin/category/add";
	}
	@PostMapping("/save")
	public ModelAndView saveOrUpdate(ModelMap model, 
			@Valid @ModelAttribute("category") CategoryModel cateModel, BindingResult result) {
		if (result.hasErrors()) {
			return new ModelAndView("forward:/admin/category",model);
		}
		Category entity = new Category();
		BeanUtils.copyProperties(cateModel, entity);
		if (entity.getImages() == "" || entity.getImages() == null)
		{
			Optional<Category> opt = cateService.findById(entity.getId());
			Category temp = opt.get();
			entity.setImages(temp.getImages());
		}
		cateService.save(entity);
		String message="";
		if (cateModel.getIsEdit() == true)
		{
			message = "Category is Edited!!!!!";
		}
		else {
			message = "Category is saved!!!!";
		}
		model.addAttribute("message",message);
		
		return new ModelAndView("forward:/admin/category",model);
	}
	
	@GetMapping("/edit/{id}")
	public ModelAndView edit(ModelMap model, @PathVariable("id") Long categoryId) {
		Optional<Category> optCategory = cateService.findById(categoryId);
		CategoryModel cateModel = new CategoryModel();
		if (optCategory.isPresent())
		{
			Category entity = optCategory.get();
			
			BeanUtils.copyProperties(entity, cateModel);
			cateModel.setIsEdit(true);
			
			model.addAttribute("category",cateModel);
			
			return new ModelAndView("admin/category/add", model);
		}
		model.addAttribute("message","Category is not existed!!");
		return new ModelAndView("forward:/admin/category",model);
		
	}
	@GetMapping("/delete/{id}")
	public ModelAndView delete(ModelMap model, @PathVariable("id") Long categoryId) {
		Optional<Category> optCategory = cateService.findById(categoryId);
		if (optCategory.isPresent())
		{
			cateService.deleteById(categoryId);
			List<Category> list = cateService.findAll();
			model.addAttribute("list",list);
			return new ModelAndView("admin/category/list", model);
		}
		model.addAttribute("message","Category is not existed!!");
		return new ModelAndView("forward:/admin/category",model);
		
	}
	
	@GetMapping("/search")
	public String search(Model model)
	{
		String name = "";
		model.addAttribute("name",name);
		return "admin/category/search";
	}
	
	@RequestMapping("/searchpaginated")
    public String search(ModelMap model,
                         @RequestParam(name="name",required = false) String name,
                         @RequestParam("page") Optional<Integer> page,
                         @RequestParam("size") Optional<Integer> size) {
        int count = (int) cateService.count();
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(3);
        Pageable pageable = PageRequest.of(currentPage-1, pageSize, Sort.by("name"));
        Page<Category> resultPage = null;
        if(StringUtils.hasText(name)) {
            resultPage = cateService.findByNameContaining(name,pageable);
            model.addAttribute("name",name);
        }else {
            resultPage = cateService.findAll(pageable);
        }
        int totalPages = resultPage.getTotalPages();
        if(totalPages > 0) {
            int start = Math.max(1, currentPage-2);
            int end = Math.min(currentPage + 2, totalPages);
            if(totalPages > count) {
                if(end == totalPages) start = end - count;
                else if (start == 1) end = start + count;
            }
            List<Integer> pageNumbers = IntStream.rangeClosed(start, end)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers",pageNumbers);
        }
        model.addAttribute("categoryPage",resultPage);
        return "admin/category/searchresult";
    }
}
