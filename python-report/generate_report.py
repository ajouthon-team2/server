import sys, json, requests, os
from docxtpl import DocxTemplate, InlineImage
from docx.shared import Mm
from io import BytesIO

# 현재 스크립트가 있는 디렉토리 기준 경로 계산
BASE_DIR = os.path.dirname(os.path.abspath(__file__))
OUTPUT_PATH = os.path.join(os.path.dirname(BASE_DIR), "운동크루보고서.docx")  # /app/운동크루보고서.docx

def fetch_image_from_url(url):
    try:
        response = requests.get(url, timeout=10)
        response.raise_for_status()
        return BytesIO(response.content)
    except Exception as e:
        print(f"❌ 이미지 다운로드 실패: {url} - {e}", file=sys.stderr)
        return None

def generate_report(data, template_path=os.path.join(BASE_DIR, "format.docx"), output_path=OUTPUT_PATH):
    doc = DocxTemplate(template_path)

    for activity in data.get("활동목록", []):
        for i in range(1, 3):
            key = f"활동증빙사진{i}"
            img_url = activity.get(key)
            if img_url and isinstance(img_url, str) and img_url.startswith("http"):
                img_file = fetch_image_from_url(img_url)
                if img_file:
                    activity[key] = InlineImage(doc, img_file, width=Mm(35))
                else:
                    activity[key] = None
            else:
                activity[key] = None

    doc.render(data)
    doc.save(output_path)
    print(f"✅ 보고서 생성 완료: {output_path}")

if __name__ == "__main__":
    input_json = sys.stdin.read()
    data = json.loads(input_json)
    generate_report(data)
