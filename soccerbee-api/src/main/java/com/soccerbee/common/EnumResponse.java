package com.soccerbee.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumResponse {
  Registered, Modified, Deleted,

  Verified, Sent, Authenticated, Requested, Added, Accepted, Delegated,

  Changed, Uploaded, Terminated, Submitted, Saved, Left;
}
