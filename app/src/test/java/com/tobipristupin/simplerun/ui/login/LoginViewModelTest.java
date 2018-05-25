package com.tobipristupin.simplerun.ui.login;

import android.arch.core.executor.testing.InstantTaskExecutorRule;

import com.tobipristupin.simplerun.ui.login.loginview.LoginViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class LoginViewModelTest {

    private LoginViewModel viewModel;

    @Before
    public void setUp(){
        viewModel = new LoginViewModel();
    }

    @Rule
    public InstantTaskExecutorRule liveDataTestRule = new InstantTaskExecutorRule();


    @Test
    public void enableLoadingAnimation(){
        assertNotEquals(new Integer(0), viewModel.getViewPagerPosition().getValue());
        viewModel.onForgotPasswordClicked();
        assertEquals(new Integer(0), viewModel.getViewPagerPosition().getValue());
    }
}
