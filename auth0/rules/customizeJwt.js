function customizeJwt(user, context, callback) {
  request.post({
    url: 'https://api.limberapp.io/jwt-claims-request',
    method: 'POST',
    headers: {
      'Authorization': `Token ${configuration.accessToken}`,
      'Content-Type': 'application/json',
      'Accept': 'application/json',
    },
    json: {
      auth0ClientId: context.clientID,
      firstName: user.given_name,
      lastName: user.family_name,
      emailAddress: user.email,
      profilePhotoUrl: user.picture,
    },
  }, (error, response, body) => {
    if (error) return callback(error);
    if (200 > response.statusCode || response.statusCode > 299) {
      return callback(new Error(`API returned ${response.statusCode} response code.`));
    }
    Object.entries(body).forEach(([key, value]) => {
      context.accessToken[`https://limberapp.io/${key}`] = value;
    });
    return callback(null, user, context);
  });
}
