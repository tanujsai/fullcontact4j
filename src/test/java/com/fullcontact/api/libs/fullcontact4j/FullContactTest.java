package com.fullcontact.api.libs.fullcontact4j;

import com.fullcontact.api.libs.fullcontact4j.entity.FullContactEntity;
import com.fullcontact.api.libs.fullcontact4j.entity.socialprofiles.SocialProfile;
import com.fullcontact.api.libs.fullcontact4j.entity.socialprofiles.SocialProfileType;
import junit.framework.TestCase;

import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: danlynn
 * Date: 2/7/12
 * Time: 5:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class FullContactTest extends TestCase {
    public static String newline = System.getProperty("line.separator");

    public void test_parse_person_json_response() throws IOException {
        String json = loadJson("lorangb@gmail.com.json");

        FullContactEntity entity = new FullContact("fake_api_key").parsePersonJsonResponse(json);

        assertEquals("Lorang", entity.getContactInfo().getFamilyName());
        assertEquals("Bart", entity.getContactInfo().getGivenName());
        assertEquals("Bart Lorang", entity.getContactInfo().getFullName());
        assertEquals(1, entity.getContactInfo().getWebsites().size());
        assertEquals("http://fullcontact.com", entity.getContactInfo().getWebsites().get(0).getUrl());

        assertNotNull(entity.getDemographics());
        assertEquals("Male", entity.getDemographics().getGender());
        assertEquals("32", entity.getDemographics().getAge());
        assertEquals("25-34", entity.getDemographics().getAgeRange());
        assertEquals("Boulder, Colorado", entity.getDemographics().getLocationGeneral());

        assertNotNull(entity.getOrganizations());
        assertEquals(2, entity.getOrganizations().size());
        assertEquals("FullContact", entity.getOrganizations().get(0).getOrganizationName());

        assertNotNull(entity.getDigitalFootprint());
        assertEquals(5, entity.getDigitalFootprint().getTopics().size());
        assertEquals(4, entity.getDigitalFootprint().getScores().size());

        assertNotNull(entity.getPhotos());
        assertEquals(7, entity.getPhotos().size());
        assertEquals("http://a0.twimg.com/profile_images/1364842224/Bart_Profile_1_normal.jpg", entity.getPhotos().get(0).getPhotoUrl());
        assertEquals("twitter", entity.getPhotos().get(0).getPhotoTypeId());

        assertNotNull(entity.getSocialProfiles());
        assertEquals(14, entity.getSocialProfiles().getAllSocialProfiles().size());
        assertEquals(631, entity.getSocialProfiles().getTwitter().getFollowers());
        assertEquals(485, entity.getSocialProfiles().getTwitter().getFollowing());
        assertEquals("http://www.twitter.com/lorangb", entity.getSocialProfiles().getTwitter().getProfileUrl());
        assertEquals("http://twitter.com/statuses/user_timeline/lorangb.rss", entity.getSocialProfiles().getTwitter().getRss());

        assertEquals("http://api.flickr.com/services/feeds/photos_public.gne?id=39267654@N00", entity.getSocialProfiles().getFlickr().getRss());
        assertEquals("Co-Founder & CEO at FullContact", entity.getSocialProfiles().getLinkedIn().getBio());
        assertEquals("lorangb", entity.getSocialProfiles().getYouTube().getProfileUsername());
        assertEquals("114426306375480734745", entity.getSocialProfiles().getPicasa().getProfileId());

    }

    public void test_parse_person_NOTHING_FOUND() throws IOException {
        String json = loadJson("notfound@gmail.com.json");
        FullContactEntity entity = new FullContact("fake_api_key").parsePersonJsonResponse(json);
        assertNull(entity.getContactInfo());
        assertNull(entity.getPhotos());
        assertNull(entity.getSocialProfiles());
        assertNull(entity.getDemographics());
        assertNull(entity.getDigitalFootprint());
        assertNull(entity.getOrganizations());
    }

    public void test_parse_person_contact_info() throws IOException {
        String json = loadJson("test.contactinfo@gmail.com.json");
        FullContactEntity entity = new FullContact("fake_api_key").parsePersonJsonResponse(json);
        assertNotNull(entity.getContactInfo());
        assertNull(entity.getOrganizations());
        assertEquals("Salil", entity.getContactInfo().getGivenName());
        assertEquals("Kalia", entity.getContactInfo().getFamilyName());
        assertEquals("Salil Kalia", entity.getContactInfo().getFullName());
        assertEquals(1, entity.getContactInfo().getWebsites().size());
        assertEquals("http://rainmaker.cc", entity.getContactInfo().getWebsites().get(0).getUrl());
    }

    public void test_parse_person_demographics_info() throws IOException {
        String json = loadJson("salil.kalia@gmail.com.json");
        FullContactEntity entity = new FullContact("fake_api_key").parsePersonJsonResponse(json);
        assertNotNull(entity.getContactInfo());
        assertEquals("29", entity.getDemographics().getAge());
        assertEquals("Delhi (NCR), India", entity.getDemographics().getLocationGeneral());
        assertEquals("Male", entity.getDemographics().getGender());
        assertEquals("25-34", entity.getDemographics().getAgeRange());
    }

    public void test_parse_person_organizations() throws IOException {
        String json = loadJson("salil.kalia@gmail.com.json");
        FullContactEntity entity = new FullContact("fake_api_key").parsePersonJsonResponse(json);
        assertNotNull(entity.getOrganizations());
        assertEquals(5, entity.getOrganizations().size());
        assertEquals(true, entity.getOrganizations().get(0).isPrimary());
        assertEquals("Tech Lead", entity.getOrganizations().get(0).getOrganizationTitle());
        assertEquals("IntelliGrape Software", entity.getOrganizations().get(0).getOrganizationName());
    }

    public void test_parse_person_social_profiles() throws IOException {
        String json = loadJson("salil.kalia@gmail.com.json");
        FullContactEntity entity = new FullContact("fake_api_key").parsePersonJsonResponse(json);
        assertNotNull(entity.getSocialProfiles());
        assertEquals(13, entity.getSocialProfiles().getAllSocialProfiles().size());
        assertEquals("28076520", entity.getSocialProfiles().getTwitter().getProfileId());
        assertEquals("http://twitter.com/statuses/user_timeline/salil_kalia.rss", entity.getSocialProfiles().getTwitter().getRss());
        assertEquals(50, entity.getSocialProfiles().getTwitter().getFollowing());
        assertEquals(64, entity.getSocialProfiles().getTwitter().getFollowers());

        assertEquals("http://www.linkedin.com/profile?viewProfile=&key=26679153", entity.getSocialProfiles().getLinkedIn().getProfileUrl());

        SocialProfile googlePlus = entity.getSocialProfiles().getSocialProfile(SocialProfileType.googleplus);
        assertNotNull(googlePlus);
        assertEquals("salilkalia", googlePlus.getProfileUsername());
        assertEquals("All time Softwares :-)<br>", googlePlus.getBio());
    }

    public void test_parse_person_photos() throws IOException {
        String json = loadJson("salil.kalia@gmail.com.json");
        FullContactEntity entity = new FullContact("fake_api_key").parsePersonJsonResponse(json);
        assertNotNull(entity.getPhotos());
        assertEquals(20, entity.getPhotos().size());
        assertEquals("http://a.vimeocdn.com/portraits/defaults/d.75.jpg", entity.getPhotos().get(1).getPhotoUrl());
        assertEquals("https://img-s.foursquare.com/userpix_thumbs/X5NY5CWU3C0R5O1B.jpg", entity.getPhotos().get(19).getPhotoUrl());
    }

    private String loadJson(String fileName) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("src/test/resources/" + fileName))));

        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
            sb.append(newline);
        }

        return sb.toString();
    }
}