function customizeJwt(user, context, callback) {
  request.post({
    url: 'https://limberapp.appspot.com/api/jwt-claims-request',
    method: 'POST',
    headers: {
      Authorization: 'Token qeJdxqpmQve0qPI0wt1sZQ==',
    },
    json: {
      firstName: user.given_name,
      lastName: user.family_name,
      emailAddress: user.email,
      profilePhotoUrl: user.picture,
    },
  }, (error, response, body) => {
    if (error) return callback(error);
    if (200 > response.statusCode || response.statusCode > 299) {
      return callback(new Error(`API returned ${response.statusCode} response code`));
    }
    Object.assign(context.accessToken, body);
    return callback(null, user, context);
  });
}
