package com.shopify;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Verb;

/**
 * The Class ShopifyServlet.
 */
@SuppressWarnings("serial")
public class ShopifyServlet extends HttpServlet
{

    /**
     * if code is not in request params i.e request is coming from scribe
     * Servlet and it needs forward to shopify server and if code is present in
     * request param then it needs to forward on scribe Servelet call back
     * 
     * @throws ServletException
     */

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException
    {

	String shop = req.getParameter("shop");
	String domain = req.getParameter("domain");
	String sessionID = req.getParameter("jsessionid");

	String code = req.getParameter("code");
	if (code == null)
	{
	    req.getSession().setAttribute("domain", domain);
	    req.getSession().setAttribute("sessionID", sessionID);
	}
	OAuthCustomService shopifyService = ShopifyUtil.getShopifyService(req);
	String shopifyServerUrl = shopifyService.getAuthorizationUrl(shop);

	if (code != null)
	{

	    String token = exchangeToken(code, shop);
	    // RequestDispatcher d =
	    // req.getRequestDispatcher(buildCallBackUrl(req, token));
	    // d.forward(req, resp);
	    resp.sendRedirect(buildCallBackUrl(req, token));
	}
	else
	{

	    resp.sendRedirect(shopifyServerUrl);
	}

    }

    /**
     * Builds the call back url.
     * 
     * @param req
     *            the req
     * @return the string
     */
    private String buildCallBackUrl(HttpServletRequest req, String token)
    {
	String domain = (String) req.getSession().getAttribute("domain");
	StringBuilder sb = new StringBuilder(domain);
	String timestamp = req.getParameter("timestamp");
	String signature = req.getParameter("signature");

	sb.append("/scribe?service=shopify&code=" + token + "&timestamp=" + timestamp + "&signature=" + signature);
	return sb.toString();

    }

    private String exchangeToken(String code, String shop)
    {
	String token = null;
	String accessURl = new ShopifyAccessURLBuilder(shop).code(code).clientKey(ShopifyUtil.SHOPIFY_API_KEY)
		.scretKey(ShopifyUtil.SHOPIFY_SECRET_KEY).buildAccessUrl();
	OAuthRequest oAuthRequest = new OAuthRequest(Verb.POST, accessURl);
	Response response = oAuthRequest.send();
	try
	{
	    HashMap<String, String> properties = new ObjectMapper().readValue(response.getBody(),
		    new TypeReference<HashMap<String, String>>()
		    {

		    });
	    token = properties.get("access_token");
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}

	return token;

    }
}
