import Html exposing (Html)

import BedmintonModel as BM
import BedmintonView as BV

main =
  Html.beginnerProgram { model = BM.model, view = BV.view, update = BM.update }
