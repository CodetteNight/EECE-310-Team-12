package org.jpacman.framework.social;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.springframework.social.DuplicateStatusException;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.twitter.api.CursoredList;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.Tweet;
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
public class TwitterController {

    private Twitter twitter;
    
    private ConnectionRepository connectionRepository;

    @Inject
    public TwitterController(Twitter twitter, ConnectionRepository connectionRepository) {
        this.twitter = twitter;     
        this.connectionRepository = connectionRepository;
    }

    @RequestMapping(method=RequestMethod.GET)
    public String helloTwitter(Model model) {
    	//Authorize App.
        if (connectionRepository.findPrimaryConnection(Twitter.class) == null) {
            return "redirect:/connect/twitter";
        }
        String points = PostToTwitter.points;
        
        System.out.println("TwitterController:helloTwitter(): points: ("+points+")");

        //Posting Tweet:
        if (points != null){
        	try{
        		DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        		String messagedate = df.format(new Date());
        		TweetData td = new TweetData("Completed a game of Jpacman. Got: "+points+" points #jpacman " + messagedate);
        		twitter.timelineOperations().updateStatus(td);
        	} catch(DuplicateStatusException dse){
        		System.out.println("#### Error: "+dse.getLocalizedMessage() );
        	}
        	PostToTwitter.points = null;
        }
        System.out.println("TwitterController:helloTwitter(): points: ("+points+")");

        //Retrieve points ??:
        //Retrieve friends list
        model.addAttribute(twitter.userOperations().getUserProfile());
        List<Tweet> tweets = twitter.timelineOperations().getUserTimeline();
        SearchResults search = twitter.searchOperations().search("#jpacman");
        List<Tweet> jpacmanTweets = search.getTweets();
        
        //CursoredList<TwitterProfile> friends = twitter.friendOperations().getFriends();
        model.addAttribute("jpacmanTweets", jpacmanTweets);
        return "startpage";
    }
    
}