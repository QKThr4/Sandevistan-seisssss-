"""Gera as texturas do mod Sandevistan (pixel art propria, no estilo dos prints enviados)."""
import os
from PIL import Image

OUT = os.path.join(os.path.dirname(__file__), "..", "src", "main", "resources", "assets", "sandevistan", "textures")
K = (0, 0, 0, 255)
G = (60, 255, 0, 255)
G2 = (30, 170, 0, 255)
R = (150, 20, 20, 255)
W = (215, 215, 215, 255)
BR = (120, 70, 40, 255)
T = (0, 0, 0, 0)


def item():
    im = Image.new("RGBA", (16, 16), T)
    px = im.load()
    # coluna de vertebras: 4 segmentos verdes com conectores pretos entre eles
    for k in range(4):
        y = k * 4
        for x in range(5, 11):
            px[x, y] = K
            px[x, y + 2] = K
        px[5, y + 1] = K
        px[10, y + 1] = K
        for x in range(6, 10):
            px[x, y + 1] = G
        px[6, y + 1] = G2
        if k < 3:
            px[7, y + 3] = K
            px[8, y + 3] = K
    return im


def back():
    # 16x24: textura do implante nas costas (8x12 pixels da pele, em dobro)
    im = Image.new("RGBA", (16, 24), T)
    px = im.load()
    for k in range(5):
        y = 2 + k * 4
        for x in range(4, 12):
            for yy in range(y, y + 2):
                px[x, yy] = K
        for x in range(6, 10):
            for yy in range(y, y + 2):
                px[x, yy] = G
        px[6, y + 1] = G2
        px[7, y + 1] = G2
        px[8, y + 1] = G2
        px[9, y + 1] = G2
        # conectores entre segmentos
        for yy in (y + 2, y + 3):
            px[7, yy] = K
            px[8, yy] = K
    # cabos vermelhos e detalhes laterais, como no print
    for x in range(2, 5):
        px[x, 11] = R
    for x in range(11, 14):
        px[x, 15] = R
    px[10, 7] = W
    px[11, 7] = W
    px[11, 12] = BR
    px[10, 12] = BR
    return im


def white():
    return Image.new("RGBA", (64, 64), (255, 255, 255, 255))


os.makedirs(os.path.join(OUT, "item"), exist_ok=True)
os.makedirs(os.path.join(OUT, "entity"), exist_ok=True)
item().save(os.path.join(OUT, "item", "sandevistan.png"))
back().save(os.path.join(OUT, "entity", "sandevistan_back.png"))
white().save(os.path.join(OUT, "entity", "afterimage.png"))
print("ok")
