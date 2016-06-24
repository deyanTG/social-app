package uni.social.connect.controller;

import ma.glasnost.orika.MapperFacade;
import uni.social.connect.Application;
import uni.social.connect.dto.UserInput;
import uni.social.connect.dto.UserOutput;
import uni.social.connect.model.User;
import uni.social.connect.service.UserService;
import uni.social.connect.utils.UserUtils;
import uni.social.connect.validation.ValidationGroups;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Tan on 18-Apr-16. User Controller
 */
@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private MapperFacade mapper;

	@RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
	public UserOutput get(@PathVariable(value = "id") Long id) {
		User user = userService.get(id);
		return mapper.map(user, UserOutput.class);
	}

	@RequestMapping(value = "/users/me", method = RequestMethod.GET)
	public UserOutput getMe() {
		User current = UserUtils.getCurrentUser();
		return mapper.map(current, UserOutput.class); 
	}

	@RequestMapping(value = "/users", method = RequestMethod.PUT)
	public UserOutput update(@RequestBody @Validated(ValidationGroups.User.Update.class) UserInput user) {
		User updatedUser = userService.update(user);
		return mapper.map(updatedUser, UserOutput.class);
	}
}
