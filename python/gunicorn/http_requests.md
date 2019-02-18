```py
import re
import StringIO
from urllib import unquote

from gunicorn import __version__
from gunicorn.errors import RequestError


NORMALIZE_SPACE = re.compile(r'(?:\r\n)?[ \t]+')

def _normalize_name(name):
    return ["-".join([w.capitalize() for w in name.split("-")])]

class HTTPRequest(object):
    SERVER_VERSION = "gunicorn/%s" % __version__
    CHUNK_SIZE = 4096
    def __init__(self, socket, address):
        self.socket = socket
        self.address = address
        self.version = None
        self.method = None
        self.path = None
        self.headers = {}
        self.fp = socket.makefile("rw", self.CHUNK_SIZE)

    def read(self):
        # 读取htt请求行
        self.first_line(self.fp.readline())
        # 读取http请求头
        self.read_headers()
        # 解析http路径和查询字符串
        if "?" in self.path:
            path_info, query = self.path.split('?', 1)
        else:
            path_info = self.path
            query = ""

        # 获取请求body的大小(从请求头中)
        length = self.body_length()
        print length
        if not length:
            wsgi_input = StringIO.StringIO()
        elif length == "chunked":
            length, wsgi_input = self.decode_chunked()
        else:
             wsgi_input = FileInput(self)

        environ = {
            "wsgi.url_scheme": 'http',
            "wsgi.input": wsgi_input,
            "wsgi.errors": sys.stderr,
            "wsgi.version": (1, 0),
            "wsgi.multithread": False,
            "wsgi.multiprocess": True,
            "wsgi.run_once": False,
            "SCRIPT_NAME": "",
            "SERVER_SOFTWARE": self.SERVER_VERSION,
            "REQUEST_METHOD": self.method,
            "PATH_INFO": unquote(path_info),
            "QUERY_STRING": query,
            "RAW_URI": self.path,
            "CONTENT_TYPE": self.headers.get('content-type', ''),
            "CONTENT_LENGTH": length,
            "REMOTE_ADDR": self.client_address[0],
            "REMOTE_PORT": self.client_address[1],
            "SERVER_NAME": self.server_address[0],
            "SERVER_PORT": self.server_address[1],
            "SERVER_PROTOCOL": self.version
        }

        for key, value in self.headers.items():
            key = 'HTTP_' + key.replace('-', '_')
            if key not in ('HTTP_CONTENT_TYPE', 'HTTP_CONTENT_LENGTH'):
                environ[key] = value

        return environ

    # 读取请求头，放在字典中
    def read_headers(self):
        hname = ""
        while True:
            line = self.fp.readline()

            if line == "\r\n": 
                # end of headers
                break
            
            if line == "\t":
                 # It's a continuation line.
                self.headers[hname] += line.strip()
            else:
                try:
                    hname =self.parse_header(line)
                except ValueError: 
                    # bad headers
                    pass
        
        
    def body_length(self):
        transfert_encoding = self.headers.get('TRANSFERT-ENCODING')
        content_length = self.headers.get('CONTENT-LENGTH')
        if transfert_encoding is None:
            if content_length is None:
                return None
            return content_length
        elif transfert_encoding == "chunked":
            return "chunked"
        else:
            return None

    def decode_chunked(self):
        '''
            分块传输入的body又多个chunk组成，每个chunk都又一个chunk-size和其内容组成：
                <chunk-size>\r\n
                <content>\r\n
                ...
            其中<chunk-size>为<content>的大小，不包括最后的\r\n。
        '''
        # 读取分块传输的数据
        length = 0
        data = StringIO.StringIO()
        while True:
            line = self.fp.readline().strip().split(";", 1)
            chunk_size = int(line.pop(0), 16)
            if chunk_size <= 0:
                break
            length += chunk_size
            data.write(self.fp.read(chunk_size))
            crlf = self.fp.read(2)
            if crlf != "\r\n":
                raise RequestError((400, "Bad chunked transfer coding "
                                         "(expected '\\r\\n', got %r)" % crlf))
                return

        # Grab any trailer headers
        self.read_headers()

        data.seek(0)
        return data, str(length) or ""

    # 
    def write(self, data):
        self.f.write(data)


    def close(self, data):
        self.fp.close()
        self.conn.close()

    # 解析请求行
    def first_line(self, line):
        method, path, version = line.split(" ")
        self.version = version
        self.method = method.upper()
        self.path = path

    # 解析请求头
    def parse_header(self, line):
        name, value = line.split(": ", 1)
        name = name.upper()
        self.headers[name] = value.strip()
        return name

class InputFile(object):
    
    def __init__(self, req):
        self.length = req.body_length()
        self.fp = req.fp
        self.eof = False
        
    def close(self):
        self.eof = False

    def read(self, amt=None):
        if self.fp is None or self.eof:
            return ''

        if amt is None:
            # unbounded read
            s = self._safe_read(self.length)
            self.close()     # we read everything
            return s

        if amt > self.length:
            amt = self.length

        s = self.fp.read(amt)
        self.length -= len(s)
        if not self.length:
            self.close()
        return s


    def readline(self, size=None):
        if self.fp is None or self.eof:
            return ''
        
        if size is not None:
            data = self.fp.readline(size)
        else:
            # User didn't specify a size ...
            # We read the line in chunks to make sure it's not a 100MB line !
            # cherrypy trick
            res = []
            while True:
                data = self.fp.readline(256)
                res.append(data)
                if len(data) < 256 or data[-1:] == "\n":
                    data = ''.join(res)
                    break
        self.length -= len(data)
        if not self.length:
            self.close()
        return data
    
    def readlines(self, sizehint=0):
        # Shamelessly stolen from StringIO
        total = 0
        lines = []
        line = self.readline()
        while line:
            lines.append(line)
            total += len(line)
            if 0 < sizehint <= total:
                break
            line = self.readline()
        return lines


    def _safe_read(self, amt):
        """Read the number of bytes requested, compensating for partial reads.
        """
        s = []
        while amt > 0:
            chunk = self.fp.read(min(amt, MAXAMOUNT))
            if not chunk:
                raise IncompleteRead(s)
            s.append(chunk)
            amt -= len(chunk)
        return ''.join(s)
        
        
    def __iter__(self):
        return self
        
    def next(self):
        if self.eof:
            raise StopIteration()
        return self.readline()
        
        
```