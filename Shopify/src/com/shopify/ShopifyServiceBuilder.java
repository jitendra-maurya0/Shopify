package com.shopify;

import java.io.OutputStream;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthConstants;
import org.scribe.model.SignatureType;
import org.scribe.oauth.OAuthService;
import org.scribe.utils.Preconditions;

/**
 * Implementation of the Builder pattern, with a fluent interface that creates a
 * {@link OAuthService}
 * 
 * @author jitendra
 * @since 2014
 * 
 */
public class ShopifyServiceBuilder
{
    private String apiKey;
    private String apiSecret;
    private String callback;
    private Api api;
    private String scope;
    private SignatureType signatureType;
    private OutputStream debugStream;

    /**
     * Default constructor
     */
    public ShopifyServiceBuilder()
    {
	this.callback = OAuthConstants.OUT_OF_BAND;
	this.signatureType = SignatureType.Header;
	this.debugStream = null;
	try
	{
	    this.api = ShopifyApi.class.newInstance();
	}
	catch (InstantiationException e)
	{

	    e.printStackTrace();
	}
	catch (IllegalAccessException e)
	{
	    e.printStackTrace();
	}
    }

    /**
     * Configures the {@link Api}
     * 
     * Overloaded version. Let's you use an instance instead of a class.
     * 
     * @param api
     *            instance of {@link Api}s
     * @return the {@link ServiceBuilder} instance for method chaining
     */
    public ShopifyServiceBuilder provider(Api api)
    {
	Preconditions.checkNotNull(api, "Api cannot be null");
	this.api = api;
	return this;
    }

    /**
     * Adds an OAuth callback url
     * 
     * @param callback
     *            callback url. Must be a valid url or 'oob' for out of band
     *            OAuth
     * @return the {@link ServiceBuilder} instance for method chaining
     */
    public ShopifyServiceBuilder callback(String callback)
    {
	Preconditions.checkNotNull(callback, "Callback can't be null");
	this.callback = callback;
	return this;
    }

    /**
     * Configures the api key
     * 
     * @param apiKey
     *            The api key for your application
     * @return the {@link ServiceBuilder} instance for method chaining
     */
    public ShopifyServiceBuilder apiKey(String apiKey)
    {
	Preconditions.checkEmptyString(apiKey, "Invalid Api key");
	this.apiKey = apiKey;
	return this;
    }

    /**
     * Configures the api secret
     * 
     * @param apiSecret
     *            The api secret for your application
     * @return the {@link ServiceBuilder} instance for method chaining
     */
    public ShopifyServiceBuilder apiSecret(String apiSecret)
    {
	Preconditions.checkEmptyString(apiSecret, "Invalid Api secret");
	this.apiSecret = apiSecret;
	return this;
    }

    /**
     * Configures the OAuth scope. This is only necessary in some APIs (like
     * Google's).
     * 
     * @param scope
     *            The OAuth scope
     * @return the {@link ServiceBuilder} instance for method chaining
     */
    public ShopifyServiceBuilder scope(String scope)
    {
	Preconditions.checkEmptyString(scope, "Invalid OAuth scope");
	this.scope = scope;
	return this;
    }

    /**
     * Configures the signature type, choose between header, querystring, etc.
     * Defaults to Header
     * 
     * @param scope
     *            The OAuth scope
     * @return the {@link ServiceBuilder} instance for method chaining
     */
    public ShopifyServiceBuilder signatureType(SignatureType type)
    {
	Preconditions.checkNotNull(type, "Signature type can't be null");
	this.signatureType = type;
	return this;
    }

    public ShopifyServiceBuilder debugStream(OutputStream stream)
    {
	Preconditions.checkNotNull(stream, "debug stream can't be null");
	this.debugStream = stream;
	return this;
    }

    public ShopifyServiceBuilder debug()
    {
	this.debugStream(System.out);
	return this;
    }

    /**
     * Returns the fully configured {@link OAuthService}
     * 
     * @return fully configured {@link OAuthService}
     */
    public OAuthCustomService build()
    {
	Preconditions.checkEmptyString(apiKey, "You must provide an api key");
	Preconditions.checkEmptyString(apiSecret, "You must provide an api secret");
	return api.createService(new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope));
    }
}
