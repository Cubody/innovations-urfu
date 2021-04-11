package com.greenorine.innovationsurfuapi.security;

import com.greenorine.innovationsurfuapi.model.User;
import com.greenorine.innovationsurfuapi.repository.UserRepository;
import com.greenorine.innovationsurfuapi.security.jwt.JwtUserFactory;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
public class UrfuAuthenticationProvider implements AuthenticationProvider {

    private final UserRepository userRepository;

    public UrfuAuthenticationProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {
            var httpClient = HttpClients.createDefault();
            var httpPost = new HttpPost("https://sts.urfu.ru/adfs/OAuth2/authorize?resource=https://istudent.urfu.ru&type=web_server" +
                    "&client_id=https://istudent.urfu.ru&redirect_uri=https://istudent.urfu.ru?auth&response_type=code&scope=");

            var login = authentication.getName();
            var password = authentication.getCredentials().toString();
            List<NameValuePair> params = new ArrayList<>(3);
            params.add(new BasicNameValuePair("UserName", login));
            params.add(new BasicNameValuePair("Password", password));
            params.add(new BasicNameValuePair("AuthMethod", "FormsAuthentication"));
            httpPost.setEntity(new UrlEncodedFormEntity(params));

            var response = httpClient.execute(httpPost);
            if (response.getStatusLine().toString().contains("200 OK") && !response.containsHeader("Set-Cookie"))
                throw new BadCredentialsException("Incorrect password or login");

            response = httpClient.execute(new HttpPost(response.getLastHeader("Location").getValue()));
            httpClient.execute(new HttpPost(response.getLastHeader("Location").getValue()));
            response = httpClient.execute(new HttpPost("https://istudent.urfu.ru/student/?auth-ok"));

            var user = userRepository.findByEmail(login);
            if (user == null) {
                var entity = response.getEntity();
                var stream = new BufferedReader(new InputStreamReader(entity.getContent(), StandardCharsets.UTF_8));
                String inputLine;
                StringBuilder answer = new StringBuilder();
                while ((inputLine = stream.readLine()) != null) {
                    answer.append(inputLine);
                }
                stream.close();
                user = new User();
                var textNodes = Jsoup.parse(answer.toString()).select(".myself p").textNodes();
                user.setEmail(textNodes.get(3).text().substring(19));
                user.setIdCard(textNodes.get(1).text().substring(24));
                user.setFullName(textNodes.get(0).text());
                userRepository.save(user);
            }
            var principal = JwtUserFactory.create(user);
            return new UsernamePasswordAuthenticationToken(principal, password, principal.getAuthorities());
        } catch (IOException e) {
            throw new AuthenticationException("Can't auth via UrFU service.") {
            };
        }
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }
}
