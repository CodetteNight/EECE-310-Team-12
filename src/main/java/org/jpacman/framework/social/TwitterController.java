package org.jpacman.framework.social;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.springframework.social.DuplicateStatusException;
import org.springframework.social.RateLimitExceededException;
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
        System.out.println("TwitterController:helloTwitter(): Establishing Connection.");
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

        //Retrieve points:
        try{
        	model.addAttribute(twitter.userOperations().getUserProfile());
        	//List<Tweet> tweets = twitter.timelineOperations().getUserTimeline();
        	//SearchResults search = twitter.searchOperations().search("#Soyuz");
        	//List<Tweet> jpacmanTweets = search.getTweets();

        	List<Tweet> tweets = twitter.timelineOperations().getHomeTimeline();
        	List<Tweet> newTweetList = new ArrayList<Tweet>();
        	int i;
        	for(i = 0; i < tweets.size(); i++){
        		if(tweets.get(i).getText().contains("#jpacman")){
        			newTweetList.add(tweets.get(i));
        		}
        	}

        	//CursoredList<TwitterProfile> friends = twitter.friendOperations().getFriends();
        	model.addAttribute("jpacmanTweets", newTweetList);
        	return "startpage";

        }catch(RateLimitExceededException ree){
        	System.out.println("RateLimit Exceeded. Post has succeded. "
        			+ "Wait 15 minutes to view Results on this webpage. "
        			+ "Alternative see your twitter feed to see new post.");
        	return "ratelimitexceeded"; //Webpage with the above error message
        }
    }
    
    @RequestMapping(value="/about", method=RequestMethod.GET)
    public String aboutPage(){
    	return "about";
    }
    
    @RequestMapping(value="/developers", method=RequestMethod.GET)
    public String developersPage(){
    	return "developers";
    }
    
    @RequestMapping(value="/disconnect", method=RequestMethod.GET)
    public String disconnectPage(){
    	return "disconnect";
    }
    
}