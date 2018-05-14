package com.company.enroller.controllers;

import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collection;

import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;
import com.company.enroller.persistence.ParticipantService;

@RunWith(SpringRunner.class)
@WebMvcTest(ParticipantRestController.class)
public class ParticipantRestControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private MeetingService meetingService;

	@MockBean
	private ParticipantService participantService;

	@Test
	public void testGetParticipants() throws Exception {
		// Given
		Participant participant = new Participant();
		participant.setLogin("testLogin");
		participant.setPassword("testPassword");

		Collection<Participant> allParticipants = singletonList(participant);

		// When
		given(participantService.getAll()).willReturn(allParticipants);

		// Then
		mvc.perform(get("/participants").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1))).andExpect(jsonPath("$[0].login", is(participant.getLogin())));
	}

	@Test
	public void testGetParticipant() throws Exception {
		// Given
		Participant participant = new Participant();
		participant.setLogin("testLogin");
		participant.setPassword("testPassword");

		// When
		given(participantService.findByLogin(participant.getLogin())).willReturn(participant);

		// Then
		mvc.perform(get("/participants/testLogin").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.login", is(participant.getLogin()))).andExpect(jsonPath("$.password", is(participant.getPassword())));
	}

	@Test
	public void testRegisterParticipant() throws Exception {
		// Given
		Participant participant = new Participant();
		participant.setLogin("testLogin");
		participant.setPassword("testPassword");

		Gson gson = new Gson();
		String jsonParticipant = gson.toJson(participant);

		// When
		given(participantService.findByLogin(participant.getLogin())).willReturn(null);

		// Then
		mvc.perform(post("/participants").contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8").content(jsonParticipant)).andExpect(status().isCreated());
	}
}
