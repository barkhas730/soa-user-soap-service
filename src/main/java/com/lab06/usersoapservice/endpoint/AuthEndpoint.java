package com.lab06.usersoapservice.endpoint;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.lab06.usersoapservice.service.AuthService;
import com.lab06.usersoapservice.service.LoginResult;
import com.lab06.usersoapservice.wsdl.GetUserIdByTokenRequest;
import com.lab06.usersoapservice.wsdl.GetUserIdByTokenResponse;
import com.lab06.usersoapservice.wsdl.LoginUserRequest;
import com.lab06.usersoapservice.wsdl.LoginUserResponse;
import com.lab06.usersoapservice.wsdl.RegisterUserRequest;
import com.lab06.usersoapservice.wsdl.RegisterUserResponse;
import com.lab06.usersoapservice.wsdl.ValidateTokenRequest;
import com.lab06.usersoapservice.wsdl.ValidateTokenResponse;

@Endpoint
public class AuthEndpoint {

    private static final String NAMESPACE_URI = "http://lab06.com/usersoapservice/auth";

    private final AuthService authService;

    public AuthEndpoint(AuthService authService) {
        this.authService = authService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "RegisterUserRequest")
    @ResponsePayload
    public RegisterUserResponse registerUser(@RequestPayload RegisterUserRequest request) {
        RegisterUserResponse response = new RegisterUserResponse();
        String message = authService.registerUser(request.getUsername(), request.getPassword());
        response.setMessage(message);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "LoginUserRequest")
    @ResponsePayload
    public LoginUserResponse loginUser(@RequestPayload LoginUserRequest request) {
        LoginResult loginResult = authService.loginUser(request.getUsername(), request.getPassword());
        LoginUserResponse response = new LoginUserResponse();
        response.setMessage(loginResult.getMessage());

        if (loginResult.isSuccess()) {
            response.setToken(loginResult.getToken());
            response.setUserId(loginResult.getUserId());
        }

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "ValidateTokenRequest")
    @ResponsePayload
    public ValidateTokenResponse validateToken(@RequestPayload ValidateTokenRequest request) {
        ValidateTokenResponse response = new ValidateTokenResponse();
        response.setValid(authService.validateToken(request.getToken()));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetUserIdByTokenRequest")
    @ResponsePayload
    public GetUserIdByTokenResponse getUserIdByToken(@RequestPayload GetUserIdByTokenRequest request) {
        GetUserIdByTokenResponse response = new GetUserIdByTokenResponse();
        Long userId = authService.getUserIdByToken(request.getToken());
        if (userId != null) {
            response.setUserId(userId);
            response.setFound(true);
        } else {
            response.setUserId(0L);
            response.setFound(false);
        }
        return response;
    }
}
