package org.jpacman.framework.social;

import javax.inject.Inject;

import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.twitter.api.CursoredList;
import org.springframework.social.twitter.api.TweetData;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Spring application to obtain tweets "#springframework".
 * From http://spring.io/guides/gs/accessing-twitter/
 */

@Controller
@RequestMapping("/")
public class TwitterController implements PostToTwitter {

    private Twitter twitter;
    
    private String points = null;

    private ConnectionRepository connectionRepository;

    @Inject
    public TwitterController(Twitter twitter, ConnectionRepository connectionRepository) {
        this.twitter = twitter;     
        this.connectionRepository = connectionRepository;
    }

    @Override
    @RequestMapping(method=RequestMethod.GET)
    public String helloTwitter(Model model) {
    	//Establish a connection if one has not been made.
        if (connectionRepository.findPrimaryConnection(Twitter.class) == null) {
            return "redirect:/connect/twitter";
        }

        //Posting Tweet:
          if (points != null){
            twitter.timelineOperations().updateStatus("Completed a game of Jpacman. Got: "+points+" points");
        }

        //Retrieve points ??:
        //Retrieve friends list
        model.addAttribute(twitter.userOperations().getUserProfile());
        CursoredList<TwitterProfile> friends = twitter.friendOperations().getFriends();
        model.addAttribute("friends", friends);
        return "startpage";
    }
    
    public String postToTwitter(Model model) {
        if (connectionRepository.findPrimaryConnection(Twitter.class) == null) {
            return "redirect:/connect/twitter";
        }

        //model.addAttribute(twitter.userOperations().getUserProfile());
        twitter.timelineOperations().updateStatus("updating1");

        return "startpage";
    }
    
    //TODO: Incomplete....
    public String disconnectTwitter(Model model) {
        if (connectionRepository.findPrimaryConnection(Twitter.class) == null) {
            return "redirect:/disconnect/twitter";
        }
        return "";
    }
    
	@Override
	public void postPoints(int p) {
		if(p == 0)
    		points = null;
		else
			points = Integer.toString(p);
	}

}