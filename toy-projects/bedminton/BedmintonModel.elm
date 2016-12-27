module BedmintonModel exposing (..)

type alias Model = {
    state: (Int, Int),
    nfunc: (Int->Int),
    icolor: String
  }

model : Model
model = {
    state = (0,0),
    nfunc = \x -> 1 * x,
    icolor = "green"
  }

leftPoint  (l,r) n = (l + n, r)
rightPoint (l,r) n = (l, r + n)


type Msg = NewGame | Left Int | Right Int | NFunc (Int->Int)

update : Msg -> Model -> Model
update msg model =
  case msg of
    NewGame -> { model | state = (0,0) }
    Left n ->  { model | state = leftPoint  model.state (model.nfunc n) }
    Right n -> { model | state = rightPoint model.state (model.nfunc n) }
    NFunc f -> { model | nfunc = f, icolor = if model.icolor == "green" then "red" else "green" }
