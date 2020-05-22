package io.limberapp.web.util

import kotlinx.css.*

@Suppress("UnusedPrivateMember")
internal object Theme {
  object Color {
    private val jet = Color("#303030")
    private val jetLighter20 = Color("#636363")

    private val babyPowder = Color("#FCFCFC")
    private val babyPowderDarker05 = Color("#EFEFEF")
    private val babyPowderDarker10 = Color("#E3E3E3")
    private val babyPowderDarker20 = Color("#C9C9C9")

    private val nickel = Color("#707070")
    private val nickelDarker10 = Color("#575757")
    private val nickelDarker20 = Color("#3D3D3D")

    private val copper = Color("#CC8C70")

    private val blue = Color("#1078D8")
    private val blueDarker10 = Color("#005FBF")
    private val blueDarker50 = Color("#000059")

    private val red = Color("#C81818")
    private val redDarker10 = Color("#AF0000")

    object Button {
      object Primary {
        val backgroundDefault = blue
        val backgroundActive = blueDarker10
        val backgroundDisabled = blueDarker50
      }

      object Secondary {
        val backgroundDefault = nickel
        val backgroundActive = nickelDarker10
        val backgroundDisabled = nickelDarker20
      }

      object Red {
        val backgroundDefault = red
        val backgroundActive = redDarker10
        val backgroundDisabled = nickelDarker20
      }
    }

    object Link {
      val default = blue
      val active = blueDarker10
      val disabled = blueDarker50
    }

    val smallActiveIndicator = copper

    object Text {
      val dark = jet
      val light = babyPowder
      val link = blue
      val red = Color.red
    }

    object Background {
      val dark = jet
      val light = babyPowder
      val lightActive = babyPowderDarker05
      val lightDisabled = babyPowderDarker10
      val link = blue
    }

    object Border {
      val dark = jetLighter20
      val light = babyPowderDarker20
    }
  }

  object ZIndex {
    const val modalFader = 1
    const val modalModal = 2
  }

  object Sizing {
    val borderRadius = 4.px
  }
}
