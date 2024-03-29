package com.boug.employee.endpoints;

import com.boug.employee.security.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@CrossOrigin(value = {"http://localhost:4200", "http://localhost", "http://localhost:4200" }, methods = {GET,POST,PUT,DELETE})
@RestController
public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    /*Already existing user
    username = admin
    password = admin*/
    @ApiOperation(value = "Used to authenticate a user. Retrieves as a result of successful authentication " +
            "information about the user and a bearer token")
    @ApiResponses(value = { @ApiResponse(code = 200 , message = ""),
            @ApiResponse(code = 400 , message = "bad request (Bad input data)"),
            @ApiResponse(code = 401 , message = "unauthorized (Bad credentials)"),
            @ApiResponse(code = 500 , message = "server error")})
    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<JwtResponse> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception
    {
        String userName = authenticationRequest.getUsername();
        authenticate(userName, authenticationRequest.getPassword() );
        final JwtUserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        final String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token,userDetails.getUsername(),
                                                 userDetails.getAccountExpire(),
                                                 userDetails.isEnabled(),
                                                 userDetails.getRegisterDate()));
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

}
