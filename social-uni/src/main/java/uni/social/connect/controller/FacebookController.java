package uni.social.connect.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.PagedList;
import org.springframework.social.facebook.api.Post;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class FacebookController {

	@Autowired(required = false)
	private Facebook facebook;

	@RequestMapping(value = "/facebook/feed", method = RequestMethod.GET)
	public PagedList<Post> showFeed(Model model) {
		return facebook.feedOperations().getFeed();
	}

	@RequestMapping(value = "/facebook/feed", method = RequestMethod.POST)
	public String postUpdate(String message) {
		facebook.feedOperations().updateStatus(message);
		return "redirect:/facebook/feed";
	}
}
