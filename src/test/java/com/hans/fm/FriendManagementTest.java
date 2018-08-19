package com.hans.fm;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.FixMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.hans.fm.model.BasicResponse;
import com.hans.fm.model.CommonFriendsRequest;
import com.hans.fm.model.ConnectRequest;
import com.hans.fm.model.GetFriendRequest;
import com.hans.fm.model.GetFriendsResponse;
import com.hans.fm.model.SendUpdateRequest;
import com.hans.fm.model.SendUpdateResponse;
import com.hans.fm.model.SubscriptionRequest;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(SpringRunner.class)
@SpringBootTest
public class FriendManagementTest {

	TestRestTemplate restTemplate = new TestRestTemplate();

	@Test
	public void a_contextLoads() {
		FriendManagement.main(new String[0]);
	}

	HttpHeaders headers = new HttpHeaders();

	public void connectFriends(String a, String b) {
		ConnectRequest req = new ConnectRequest();
		ArrayList<String> friends = new ArrayList<String>();
		friends.add(a);
		friends.add(b);
		req.setFriends(friends);

		HttpEntity<ConnectRequest> entity = new HttpEntity<ConnectRequest>(req, headers);

		ResponseEntity<BasicResponse> response = restTemplate.exchange(createURLWithPort("friend/connect"),
				HttpMethod.POST, entity, BasicResponse.class);

	}

	@Test
	public void b_connectFriends() {
		ConnectRequest req = new ConnectRequest();
		ArrayList<String> friends = new ArrayList<String>();
		friends.add("userA@hans.com");
		friends.add("userB@hans.com");
		req.setFriends(friends);

		HttpEntity<ConnectRequest> entity = new HttpEntity<ConnectRequest>(req, headers);

		ResponseEntity<BasicResponse> response = restTemplate.exchange(createURLWithPort("friend/connect"),
				HttpMethod.POST, entity, BasicResponse.class);

		BasicResponse res = response.getBody();

		assertTrue(res.getSuccess() == true);
	}

	@Test
	public void c_getFriends() {
		GetFriendRequest req = new GetFriendRequest();
		req.setEmail("userA@hans.com");

		HttpEntity<GetFriendRequest> entity = new HttpEntity<GetFriendRequest>(req, headers);

		ResponseEntity<GetFriendsResponse> response = restTemplate.exchange(createURLWithPort("friend/list"),
				HttpMethod.POST, entity, GetFriendsResponse.class);

		GetFriendsResponse res = response.getBody();

		assertTrue(res.getSuccess() == true && res.getFriends().size() > 0 && res.getCount() > 0);
	}

	@Test
	public void d_getCommons() {

		// add C as another A friends
		this.connectFriends("userA@hans.com", "userC@hans.com");

		CommonFriendsRequest req = new CommonFriendsRequest();
		ArrayList<String> friends = new ArrayList<String>();
		friends.add("userB@hans.com");
		friends.add("userC@hans.com");
		req.setFriends(friends);

		HttpEntity<CommonFriendsRequest> entity = new HttpEntity<CommonFriendsRequest>(req, headers);

		ResponseEntity<GetFriendsResponse> response = restTemplate.exchange(createURLWithPort("friend/common"),
				HttpMethod.POST, entity, GetFriendsResponse.class);

		GetFriendsResponse res = response.getBody();

		assertTrue(res.getSuccess() == true && res.getFriends().size() > 0 && res.getCount() == 1
				&& res.getFriends().get(0).equals("userA@hans.com"));
	}

	@Test
	public void e_subscribing() {
		SubscriptionRequest req = new SubscriptionRequest();
		req.setRequestor("userD@hans.com");
		req.setTarget("userA@hans.com");

		HttpEntity<SubscriptionRequest> entity = new HttpEntity<SubscriptionRequest>(req, headers);

		ResponseEntity<BasicResponse> response = restTemplate.exchange(createURLWithPort("friend/subscribe"),
				HttpMethod.POST, entity, BasicResponse.class);

		BasicResponse res = response.getBody();

		assertTrue(res.getSuccess() == true);
	}

	@Test
	public void f_sendUpdate() {
		SendUpdateRequest req = new SendUpdateRequest();
		req.setSender("userA@hans.com");
		req.setText("Hi userF@hans.com");

		HttpEntity<SendUpdateRequest> entity = new HttpEntity<SendUpdateRequest>(req, headers);

		ResponseEntity<SendUpdateResponse> response = restTemplate.exchange(createURLWithPort("friend/send"),
				HttpMethod.POST, entity, SendUpdateResponse.class);

		SendUpdateResponse res = response.getBody();

		assertTrue(res.getSuccess() == true && res.getRecipients().contains("userB@hans.com")
				&& res.getRecipients().contains("userC@hans.com") && res.getRecipients().contains("userD@hans.com")
				&& res.getRecipients().contains("userF@hans.com"));
	}

	@Test
	public void g_blockFriend() {
		SubscriptionRequest req = new SubscriptionRequest();
		req.setRequestor("userC@hans.com");
		req.setTarget("userA@hans.com");
		
		HttpEntity<SubscriptionRequest> entity = new HttpEntity<SubscriptionRequest>(req, headers);

		ResponseEntity<BasicResponse> response = restTemplate.exchange(createURLWithPort("friend/block"),
				HttpMethod.POST, entity, BasicResponse.class);

		BasicResponse res = response.getBody();

		assertTrue(res.getSuccess() == true);
	}
	
	@Test
	public void h_sendUpdateAfterBlock() {
		SendUpdateRequest req = new SendUpdateRequest();
		req.setSender("userA@hans.com");
		req.setText("Hi userF@hans.com");

		HttpEntity<SendUpdateRequest> entity = new HttpEntity<SendUpdateRequest>(req, headers);

		ResponseEntity<SendUpdateResponse> response = restTemplate.exchange(createURLWithPort("friend/send"),
				HttpMethod.POST, entity, SendUpdateResponse.class);

		SendUpdateResponse res = response.getBody();

		assertTrue(res.getSuccess() == true && res.getRecipients().contains("userB@hans.com")
				&& res.getRecipients().contains("userD@hans.com")
				&& res.getRecipients().contains("userF@hans.com"));
	}
	
	@Test
	public void i_blockFriend2() {
		SubscriptionRequest req = new SubscriptionRequest();
		req.setRequestor("userC@hans.com");
		req.setTarget("userH@hans.com");
		
		HttpEntity<SubscriptionRequest> entity = new HttpEntity<SubscriptionRequest>(req, headers);

		ResponseEntity<BasicResponse> response = restTemplate.exchange(createURLWithPort("friend/block"),
				HttpMethod.POST, entity, BasicResponse.class);

		BasicResponse res = response.getBody();

		assertTrue(res.getSuccess() == true);
	}
	
	@Test
	public void j_connectFriendsAfterBlock() {
		ConnectRequest req = new ConnectRequest();
		ArrayList<String> friends = new ArrayList<String>();
		friends.add("userC@hans.com");
		friends.add("userH@hans.com");
		req.setFriends(friends);

		HttpEntity<ConnectRequest> entity = new HttpEntity<ConnectRequest>(req, headers);

		ResponseEntity<BasicResponse> response = restTemplate.exchange(createURLWithPort("friend/connect"),
				HttpMethod.POST, entity, BasicResponse.class);

		BasicResponse res = response.getBody();

		assertTrue(res.getSuccess() == false);
	}

	private String createURLWithPort(String uri) {
		return "http://localhost:8000" + uri;
	}

}
