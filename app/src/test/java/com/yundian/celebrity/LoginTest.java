package com.yundian.celebrity;

import android.view.View;

import com.yundian.celebrity.ui.main.activity.LoginActivity;
import com.yundian.celebrity.widget.WPEditText;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class LoginTest {
    //    @Test
//    public void addition_isCorrect() throws Exception {
//        assertEquals(4, 2 + 2);
//    }
    @Before
    public void setup() {
        LoginActivity loginActivity = Robolectric.setupActivity(LoginActivity.class);
        WPEditText userNameEditText = (WPEditText)loginActivity.findViewById(R.id.userNameEditText);
    }

    @Test
    @Spec(desc = "should loging use CheckHelper")
    public void testLogin() {
        LoginActivity loginActivity = Mockito.spy(LoginActivity.class);
        loginActivity.loging("user");
    }
}