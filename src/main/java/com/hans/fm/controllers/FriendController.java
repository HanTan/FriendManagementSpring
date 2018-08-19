package com.hans.fm.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hans.fm.model.BasicResponse;
import com.hans.fm.model.CommonFriendsRequest;
import com.hans.fm.model.ConnectRequest;
import com.hans.fm.model.GetFriendRequest;
import com.hans.fm.model.GetFriendsResponse;
import com.hans.fm.model.SendUpdateRequest;
import com.hans.fm.model.SendUpdateResponse;
import com.hans.fm.model.SubscriptionRequest;
import com.hans.fm.model.User;
import com.hans.fm.repositories.FriendRepository;

@RestController
@RequestMapping("/friend")
public class FriendController {

	@Autowired
	FriendRepository friendRepository;

	@RequestMapping(value = "/connect", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody BasicResponse connectFriends(@RequestBody ConnectRequest request) {

		BasicResponse response = new BasicResponse();

		if (request.getFriends().size() != 2) {
			response.setSuccess(false);
			return response;
		}

		User userA = friendRepository.findByEmail(request.getFriends().get(0));
		User userB = friendRepository.findByEmail(request.getFriends().get(1));

		//user not found
		if (null == userA || null == userB) {
			response.setSuccess(false);
			return response;
		}

		//if any of blocking is found, then add friend is not allowed
		if (userA.getBlocked().contains(userB.getEmail()) || userB.getBlocked().contains(userA.getEmail())) {
			response.setSuccess(false);
			return response;
		}

		userA.addFriend(userB.getEmail());
		userB.addFriend(userA.getEmail());
		friendRepository.saveAndFlush(userA);
		friendRepository.saveAndFlush(userB);

		response.setSuccess(true);
		return response;
	}

	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody GetFriendsResponse getFriends(@RequestBody GetFriendRequest request) {
		GetFriendsResponse response = new GetFriendsResponse();

		if (request.getEmail().equalsIgnoreCase(" ")) {
			response.setSuccess(false);
			return response;
		}

		User user = friendRepository.findByEmail(request.getEmail());

		response.setCount(user.getFriends().size());
		response.setFriends(user.getFriends());
		response.setSuccess(true);

		return response;
	}

	@RequestMapping(value = "/common", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody GetFriendsResponse commonFriends(@RequestBody CommonFriendsRequest request) {
		GetFriendsResponse response = new GetFriendsResponse();

		if (request.getFriends().size() != 2) {
			response.setSuccess(false);
			return response;
		}

		String userAemail = request.getFriends().get(0);
		String userBemail = request.getFriends().get(1);

		User userA = friendRepository.findByEmail(userAemail);
		User userB = friendRepository.findByEmail(userBemail);

		List<String> commonFriends = new ArrayList<>();
		for (String user : userA.getFriends()) {
			for (String us : userB.getFriends()) {
				if (user.equalsIgnoreCase(us)) {
					commonFriends.add(user);
				}
			}
		}

		response.setCount(commonFriends.size());
		response.setFriends(commonFriends);
		response.setSuccess(true);

		return response;
	}

	@RequestMapping(value = "/subscribe", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody BasicResponse subscribeFriend(@RequestBody SubscriptionRequest request) {

		BasicResponse response = new BasicResponse();

		User user = friendRepository.findByEmail(request.getRequestor());
		if (user != null) {
			user.addSubscription(request.getTarget());
			friendRepository.saveAndFlush(user);
			response.setSuccess(true);
		}

		return response;
	}

	@RequestMapping(value = "/block", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody BasicResponse blockFriend(@RequestBody SubscriptionRequest request) {

		BasicResponse response = new BasicResponse();

		User user = friendRepository.findByEmail(request.getRequestor());
		if (user != null) {
			user.block(request.getTarget());
			friendRepository.saveAndFlush(user);
			response.setSuccess(true);
		}

		return response;
	}

	@RequestMapping(value = "/send", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody SendUpdateResponse sendUpdate(@RequestBody SendUpdateRequest request) {
		SendUpdateResponse response = new SendUpdateResponse();

		User sender = friendRepository.findByEmail(request.getSender());

		if (sender != null) {
			List<String> recipients = new ArrayList<>();

			List<User> allUsers = friendRepository.findAll();
			for (User user : allUsers) {
				boolean isBlocked = user.getBlocked().contains(sender.getEmail());

				if (!isBlocked) {
					boolean isFriend = user.getFriends().contains(sender.getEmail());
					boolean isSubscriber = user.getSubscriptions().contains(sender.getEmail());
					boolean isMentioned = request.getText().contains(user.getEmail());
					if (isFriend || isSubscriber || isMentioned)
						recipients.add(user.getEmail());
				}
			}
			response.setRecipients(recipients);
			response.setSuccess(true);

		}

		return response;
	}

}
