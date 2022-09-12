/**
 * This firewall implements a derived version of the OWASP ModSecurity Core Rule Set.
 * See https://owasp.org/www-project-modsecurity-core-rule-set for more information.
 */

resource "google_compute_security_policy" "default" {
  name = "default"
  rule {
    action   = "deny(502)"
    priority = "1000"
    match {
      expr {
        expression = file("firewall/sqli-stable.txt")
      }
    }
    description = "sqli-stable"
  }
  rule {
    action   = "deny(502)"
    priority = "2000"
    match {
      expr {
        expression = file("firewall/xss-stable.txt")
      }
    }
    description = "xss-stable"
  }
  rule {
    action   = "deny(502)"
    priority = "3000"
    match {
      expr {
        expression = file("firewall/lfi-stable.txt")
      }
    }
    description = "lfi-stable"
  }
  rule {
    action   = "deny(502)"
    priority = "4000"
    match {
      expr {
        expression = file("firewall/rce-stable.txt")
      }
    }
    description = "rce-stable"
  }
  rule {
    action   = "deny(502)"
    priority = "5000"
    match {
      expr {
        expression = file("firewall/rfi-stable.txt")
      }
    }
    description = "rfi-stable"
  }
  rule {
    action   = "deny(502)"
    priority = "6000"
    match {
      expr {
        expression = file("firewall/methodenforcement-stable.txt")
      }
    }
    description = "methodenforcement-stable"
  }
  rule {
    action   = "deny(502)"
    priority = "7000"
    match {
      expr {
        expression = file("firewall/scannerdetection-stable.txt")
      }
    }
    description = "scannerdetection-stable"
  }
  rule {
    action   = "deny(502)"
    priority = "8000"
    match {
      expr {
        expression = file("firewall/protocolattack-stable.txt")
      }
    }
    description = "protocolattack-stable"
  }
  rule {
    action   = "deny(502)"
    priority = "9000"
    match {
      expr {
        expression = file("firewall/php-stable.txt")
      }
    }
    description = "php-stable"
  }
  rule {
    action   = "deny(502)"
    priority = "10000"
    match {
      expr {
        expression = file("firewall/sessionfixation-stable.txt")
      }
    }
    description = "sessionfixation-stable"
  }
  rule {
    action   = "allow"
    priority = "2147483647"
    match {
      versioned_expr = "SRC_IPS_V1"
      config {
        src_ip_ranges = ["*"]
      }
    }
    description = "Default rule, higher priority overrides it"
  }
}
