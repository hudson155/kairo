exports.onExecutePostLogin = async (event, api) => {
  const claims = getClaims(event.user);
  setClaims(api.accessToken, claims);
};

const getClaims = (user) => {
  const appMetadata = user.app_metadata;
  const encoded = appMetadata.claims;
  const decoded = atob(encoded);
  return JSON.parse(decoded);
};

const setClaims = (accessToken, claims) => {
  for (const [key, value] of Object.entries(claims)) {
    accessToken.setCustomClaim(key, value);
  }
};
