VERSION 5.00
Begin VB.Form Form1 
   Caption         =   "Magick˝ ötvorec"
   ClientHeight    =   4725
   ClientLeft      =   60
   ClientTop       =   345
   ClientWidth     =   6030
   LinkTopic       =   "Form1"
   ScaleHeight     =   4725
   ScaleWidth      =   6030
   StartUpPosition =   3  'Windows Default
   Begin VB.PictureBox Picture1 
      AutoRedraw      =   -1  'True
      BeginProperty Font 
         Name            =   "Courier New"
         Size            =   8.25
         Charset         =   238
         Weight          =   400
         Underline       =   0   'False
         Italic          =   0   'False
         Strikethrough   =   0   'False
      EndProperty
      Height          =   3840
      Left            =   150
      ScaleHeight     =   3780
      ScaleWidth      =   4380
      TabIndex        =   4
      Top             =   750
      Width           =   4440
   End
   Begin VB.CommandButton Command1 
      Caption         =   "VypoËÌtaù"
      Default         =   -1  'True
      Height          =   390
      Left            =   3000
      TabIndex        =   3
      Top             =   300
      Width           =   1665
   End
   Begin VB.TextBox txtR 
      Height          =   315
      Left            =   1800
      TabIndex        =   1
      Text            =   "0"
      Top             =   375
      Width           =   615
   End
   Begin VB.Label lblCount 
      AutoSize        =   -1  'True
      Caption         =   "0"
      Height          =   195
      Left            =   675
      TabIndex        =   6
      Top             =   75
      Width           =   90
   End
   Begin VB.Label l 
      AutoSize        =   -1  'True
      Caption         =   "S˙Ëet:"
      Height          =   195
      Left            =   150
      TabIndex        =   5
      Top             =   75
      Width           =   465
   End
   Begin VB.Label lblR 
      AutoSize        =   -1  'True
      Caption         =   "x 0"
      Height          =   195
      Left            =   2475
      TabIndex        =   2
      Top             =   450
      Width           =   210
   End
   Begin VB.Label Label1 
      AutoSize        =   -1  'True
      Caption         =   "Vloûte rozmer ötvorca:"
      Height          =   195
      Left            =   150
      TabIndex        =   0
      Top             =   450
      Width           =   1560
   End
End
Attribute VB_Name = "Form1"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Option Explicit

Dim M() As Long

Private Sub Command1_Click()
Picture1.Cls
Dim I As Long, J As Long
Dim I1 As Long, J1 As Long
Dim x As Long, y As Long
Dim N As Long

If txtR.Text = "0" Then
    MsgBox "Nezadali ste spr·vnu veækosù ötvorca !"
    Exit Sub
End If

N = Val(txtR.Text)

'kontrola parity
If N \ 2 = N / 2 Then
    MsgBox "Rozmer ötvorca je p·rne ËÌslo !"
    Exit Sub
End If

ReDim M(N, N)

For x = 1 To N
    For y = 1 To N
        M(x, y) = 0
    Next y
Next x

I = (N + 1) \ 2 + 1
J = 2

For x = 1 To N * N
    If I = 1 Then
        I1 = N
    Else
        I1 = I - 1
    End If
    If J = 1 Then
        J1 = N
    Else
        J1 = J - 1
    End If
    
    If M(J1, I1) <> 0 Then
        J = J + 1
    Else
        I = I1
        J = J1
    End If
    
    M(J, I) = x
Next x

Picture1.CurrentY = 100
Dim H As Long
Dim F As Boolean
F = False
H = 0

For x = 1 To N
    For y = 1 To N
        Picture1.Print M(x, y);
        If F = False Then
            H = H + M(x, y)
        End If
    Next y
    Picture1.Print
    F = True
Next x

lblCount.Caption = H
End Sub

Private Sub Form_Resize()
Picture1.Width = Me.Width - Picture1.Left - 250
Picture1.Height = Me.Height - Picture1.Top - 550
End Sub

Private Sub txtR_Change()
lblR.Caption = "x " & txtR.Text
End Sub

Private Sub txtR_KeyPress(KeyAscii As Integer)
Select Case KeyAscii

    Case vbKey0 To vbKey9, vbKeyBack
    Case Else: KeyAscii = 0

End Select
End Sub
