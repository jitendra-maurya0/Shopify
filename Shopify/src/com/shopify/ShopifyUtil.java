/**
 * 
 */
package com.shopify;

import javax.servlet.http.HttpServletRequest;

/**
 * @author jitendra
 * 
 */
public class ShopifyUtil
{

    /** The Constant SHOPIFY_API_KEY. */
    public static final String SHOPIFY_API_KEY = "66198cc5eb16795088bfc0a3f8d2c011";

    /** The Constant SHOPIFY_SECRET_KEY. */
    public static final String SHOPIFY_SECRET_KEY = "68e7c78529457be317dde3807b67de0b";

    /** The Constant SHOPIFY_SCOPE. */
    private static final String SHOPIFY_SCOPE = "read_customers, read_orders,read_products";

    /**
     * Gets the shopify service.
     * 
     * @param req
     *            the req
     * @param res
     *            the res
     * @param serviceType
     *            the service type
     * @return the shopify service
     */
    public static OAuthCustomService getShopifyService(HttpServletRequest req)
    {

	return getShopifyCustomService(req, SHOPIFY_API_KEY, SHOPIFY_SECRET_KEY, SHOPIFY_SCOPE);

    }

    /**
     * Gets the shopify custom service.
     * 
     * @param req
     *            the req
     * @param apiClass
     *            the api class
     * @param apiKey
     *            the api key
     * @param apiSecret
     *            the api secret
     * @param scope
     *            the scope
     * @return the shopify custom service
     */
    private static OAuthCustomService getShopifyCustomService(HttpServletRequest req, String apiKey, String apiSecret,
	    String scope)
    {

	return new ShopifyServiceBuilder().apiKey(apiKey).apiSecret(apiSecret).scope(scope).build();

    }

}
