package org.craftedsw.tripservicekata.trip;

import org.craftedsw.tripservicekata.exception.UserNotLoggedInException;
import org.craftedsw.tripservicekata.user.User;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class TripServiceTest {

    private static final List<Trip> NO_TRIPS = Collections.emptyList();
    private static final Trip A_TRIP_TO_BRAZIL = new Trip();
    private static final Trip A_TRIP_TO_BARCELONA = new Trip();
    private static final User A_THIRD_USER = new User();
    private final User NOT_LOGGED_IN_USER = null;
    private final User LOGGED_IN_USER = new User();
    private List<Trip> trips;
    private User sessionUser;
    private final User ANOTHER_USER = new User();
    private TestableTripService service;

    @Before
    public void setUp() {
        service = new TestableTripService();
    }

    @Test(expected = UserNotLoggedInException.class)
    public void should_throw_exception_when_logged_user_is_null() {
        sessionUser = NOT_LOGGED_IN_USER;

        service.getTripsByUser(sessionUser);
    }

    @Test
    public void should_return_empty_list_when_user_has_no_friends() {
        sessionUser = LOGGED_IN_USER;

        assertTrue(service.getTripsByUser(sessionUser).isEmpty());
    }

    @Test
    public void should_return_empty_list_when_a_user_has_a_friend_but_is_not_session_user() {
        sessionUser = LOGGED_IN_USER;
        User anotherUser = ANOTHER_USER;
        anotherUser.addFriend(A_THIRD_USER);

        assertTrue(service.getTripsByUser(anotherUser).isEmpty());
    }

    @Test
    public void should_return_empty_list_when_a_friend_has_no_trips() {
        sessionUser = LOGGED_IN_USER;
        User anotherUser = ANOTHER_USER;
        anotherUser.addFriend(sessionUser);
        trips = NO_TRIPS;

        assertTrue(service.getTripsByUser(anotherUser).isEmpty());
    }

    @Test
    public void should_return_list_of_trips_when_a_friend_has_2_trips() {
        sessionUser = LOGGED_IN_USER;
        User anotherUser = ANOTHER_USER;
        anotherUser.addFriend(sessionUser);
        trips = TwoTrips();

        assertEquals(2, service.getTripsByUser(anotherUser).size());
    }

    protected List<Trip> TwoTrips() {
        return Arrays.asList(A_TRIP_TO_BRAZIL, A_TRIP_TO_BARCELONA);
    }

    private class TestableTripService extends TripService {

        @Override
        protected User getLoggedUser() {
            return sessionUser;
        }

        @Override
        protected List<Trip> findTripsByUser(User user) {
            return trips;
        }
    }
}
