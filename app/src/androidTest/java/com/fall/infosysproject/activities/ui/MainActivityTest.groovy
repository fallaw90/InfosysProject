package com.fall.infosysproject.activities.ui

import android.widget.GridView
import androidx.test.espresso.Espresso
import androidx.test.rule.ActivityTestRule
import com.fall.infosysproject.activities.ui.MainActivity
import com.fall.infosysproject.test.R
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    private String searchTerm = "Dog";
    private GridView gridView;

    @Before
    void setUp() {
        super.setUp()
    }

    @Test
    void testUserInputScenario(){
        //Input some text in the EditText
        Espresso.onView(withId(R.id.et_search)).perform(typeText(searchTerm));
        //Close soft keyboard
        Espresso.closeSoftKeyboard();
        //Perform button click
        Espresso.onView(withId(R.id.bt_search)).perform(click());
        //Checking the result
        onData(anything()).inAdapterView(withId(R.id.gridview)).atPosition(0).
                onChildView(withId(R.id.item_image)).perform(click());
    }

    @After
    void tearDown() {
    }
}
