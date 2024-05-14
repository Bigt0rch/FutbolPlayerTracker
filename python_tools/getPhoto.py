import requests
from bs4 import BeautifulSoup
import sys


def get_photo(url):
    html = requests.get(url).content
    soup = BeautifulSoup(html, "html.parser")

    # find the image
    image = soup.find("meta", property="og:image")["content"]

    return image

def save_photo(image, output_file):
    if not image:
        print("No image found")
        return
    if ".png" not in output_file:
        output_file += ".png"
    with open(output_file, "wb") as f:
        f.write(requests.get(image).content)

if __name__ == "__main__":
    # Use: getPhoto.py <url> <output_file>
    if len(sys.argv) < 2:
        print("Uso: python python_tools/getPhoto.py <url> (<output_file>)")
        sys.exit(1)
    url = sys.argv[1]
    output_file = sys.argv[2] if len(sys.argv) == 3 else "output.png"
    photo = get_photo(url)
    save_photo(photo, output_file)


    
