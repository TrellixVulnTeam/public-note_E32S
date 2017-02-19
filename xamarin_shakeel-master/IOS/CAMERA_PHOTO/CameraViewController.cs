using Foundation;
using System;
using UIKit;
using AVFoundation;
using CoreFoundation;
using CoreMedia;
using CoreGraphics;
using CoreVideo;
using Photos;
using System.IO;

namespace FaceCareApp
{
    public partial class CameraViewController : UIViewController
    {
        public CameraViewController (IntPtr handle) : base (handle)
        {
        }

        public override void ViewDidLoad()
        {
            base.ViewDidLoad();

            this.imageViewVideo.BackgroundColor = UIColor.Black;    //���ñ���ɫ
            //this.imageViewVideo.ContentMode = UIViewContentMode.ScaleAspectFit;

            //View��ʾ�����Ϳ�ʼ��ʾ��Ƶ�ˣ������ť�Ĺ����Ǳ�����Ƭ
            this.btnSnap.TouchUpInside += BtnSnap_TouchUpInside;
            this.btnSnap.ImageView.Image.ImageWithRenderingMode(UIImageRenderingMode.AlwaysOriginal);
        }

        public override void ViewWillAppear(bool animated)
        {   //View��ʾ��ʱ�򣬾����ú�
            base.ViewWillAppear(animated);
            //�ȴ�������ViewWillAppear
            this.TabBarController.TabBar.Hidden = true;
            SetupCaptureSession();                                //�������˾Ϳ���
        }

        public override void ViewWillDisappear(bool animated)
        {   //View��ʧ��ʱ�򣬾�ֹͣ
            base.ViewWillDisappear(animated);
            this.TabBarController.TabBar.Hidden = false;
            StopCaptureSession();
        }

        private DispatchQueue m_queue = new DispatchQueue("myQueue");
        private AVCaptureSession m_session = new AVCaptureSession();            //���а�������ƵԴ���Լ���Ƶ���
        private AVCaptureDevice m_videoDevice = null;                           //��ƵԴ���豸������ʹ�õ�������ͷ
        private AVCaptureStillImageOutput m_stillImageOutput = null;            //����session���е�һ��������������jpegѹ����

        async void SetupCaptureSession()
        {
            var granted = await AVCaptureDevice.RequestAccessForMediaTypeAsync(AVMediaType.Video);
            if (granted)
            {
                //AVCaptureDevice captureDevice = AVCaptureDevice.DefaultDeviceWithMediaType(AVMediaType.Video);
                AVCaptureDevice videoDevice = null;
                AVCaptureDevice[] devices = AVCaptureDevice.DevicesWithMediaType(AVMediaType.Video);
                foreach (var item in devices)
                {
                    if (item.Position == AVCaptureDevicePosition.Back)
                    {
                        videoDevice = item;
                        break;
                    }
                }
                if (videoDevice == null)
                {
                    new UIAlertView("��ʾ", "��ȡ����ͷʧ�ܣ�", null, "ȷ��").Show();
                    return;
                }

                AVCaptureDeviceInput videoInput = AVCaptureDeviceInput.FromDevice(videoDevice); //��ƵԴ��Ϊ����ͷ
                if (videoInput == null)
                {
                    new UIAlertView("��ʾ", "��ȡ����ͷ��ƵԴʧ�ܣ�", null, "ȷ��").Show();
                    return;
                }

                AVCaptureVideoDataOutput videoOutput = new AVCaptureVideoDataOutput();
                videoOutput.WeakVideoSettings = new CVPixelBufferAttributes() { PixelFormatType = CVPixelFormatType.CV32BGRA }.Dictionary;
                videoOutput.MinFrameDuration = new CMTime(1, 15);  // 15fps
                videoOutput.SetSampleBufferDelegateQueue(new CameraVideoTransform(this.imageViewVideo), m_queue);       //�����imageViewVideo,����ÿһ֡��CameraVideoTransform����һ���任

                AVCaptureStillImageOutput stillImageOutput = new AVCaptureStillImageOutput();
                stillImageOutput.CompressedVideoSetting = new AVVideoSettingsCompressed() { Codec = AVVideoCodec.JPEG };

                m_session.BeginConfiguration();
                m_session.SessionPreset = AVCaptureSession.PresetMedium;
                m_session.AddInput(videoInput);
                //�������������
                //videoOutput��ί�и�CameraVideoTransform�����.
                //stillImageOutput, û��ί�и������������������������ĸ�ʽ�Ǳ�jpegѹ������.
                m_session.AddOutput(videoOutput);
                m_session.AddOutput(stillImageOutput);
                m_session.CommitConfiguration();

                m_queue.DispatchAsync(delegate ()
                {
                    m_session.StartRunning();       //����
                });

                m_videoDevice = videoDevice;
                m_stillImageOutput = stillImageOutput;
            }
            else
            {
                new UIAlertView("��ʾ", "û�з�������ͷ��Ȩ�ޣ�", null, "ȷ��").Show();
                //this.NavigationController.PopViewController(true);
                return;
            }
        }

        void StopCaptureSession()
        {
            m_queue.DispatchAsync(delegate()
            {
                m_session.StopRunning();
            });
        }

        private void BtnSnap_TouchUpInside(object sender, EventArgs e)
        {
            SnapStillImage();
        }

        private async void SnapStillImage()
        {
            //
            if ((m_videoDevice != null) && (m_stillImageOutput != null))
            {
                if (m_videoDevice.HasFlash && m_videoDevice.IsFlashModeSupported(AVCaptureFlashMode.Auto))
                {
                    NSError error;
                    if (m_videoDevice.LockForConfiguration(out error))
                    {
                        m_videoDevice.FlashMode = AVCaptureFlashMode.Auto;
                        m_videoDevice.UnlockForConfiguration();
                    }
                }

                AVCaptureConnection connection = m_stillImageOutput.ConnectionFromMediaType(AVMediaType.Video);
                var imageDataSampleBuffer = await m_stillImageOutput.CaptureStillImageTaskAsync(connection);        //��õ�ǰ֡��ѹ��ͼ��
                var imageData = AVCaptureStillImageOutput.JpegStillToNSData(imageDataSampleBuffer);                 //�õ���ǰ֡ѹ��ͼ���ͼ������...

                //RequestAuthorization(handler), handler���û���Ȩ�޶Ի��򽻻���ִ�еĶ�����
                PHPhotoLibrary.RequestAuthorization(status => {
                    
                    if (status == PHAuthorizationStatus.Authorized)
                    {   // ���û���Ȩ��

                        // To preserve the metadata, we create an asset from the JPEG NSData representation.
                        // Note that creating an asset from a UIImage discards the metadata.

                        // In iOS 9, we can use AddResource method on PHAssetCreationRequest class.
                        // In iOS 8, we save the image to a temporary file and use +[PHAssetChangeRequest creationRequestForAssetFromImageAtFileURL:].

                        if (UIDevice.CurrentDevice.CheckSystemVersion(9, 0))
                        {
                            //PHPhotoLibrary.SharedPhotoLibrary ���ص���һ��(����)ͼƬ�����
                            //PerformChanges (changeHandler, completionHandler) changeHandler �Լ� completionHandler ��һ��lambda
                            PHPhotoLibrary.SharedPhotoLibrary.PerformChanges(() => {
                                var request = PHAssetCreationRequest.CreationRequestForAsset();
                                request.AddResource(PHAssetResourceType.Photo, imageData, null);        //���浱ǰ��Ƭ
                            }, (success, err) => {
                                if (!success)
                                {
                                    Console.WriteLine("Error occurred while saving image to photo library: {0}", err);
                                }
                            });
                        }
                        else
                        {   //�û�û����Ȩ

                            string outputFileName = NSProcessInfo.ProcessInfo.GloballyUniqueString;
                            string tmpDir = Path.GetTempPath();
                            string outputFilePath = Path.Combine(tmpDir, outputFileName);
                            string outputFilePath2 = Path.ChangeExtension(outputFilePath, "jpg");
                            NSUrl temporaryFileUrl = new NSUrl(outputFilePath2, false);

                            PHPhotoLibrary.SharedPhotoLibrary.PerformChanges(() => {
                                NSError error = null;
                                if (imageData.Save(temporaryFileUrl, NSDataWritingOptions.Atomic, out error))
                                {
                                    PHAssetChangeRequest.FromImage(temporaryFileUrl);
                                }
                                else
                                {
                                    Console.WriteLine("Error occured while writing image data to a temporary file: {0}", error);
                                }
                            }, (success, error) => {
                                if (!success)
                                {
                                    Console.WriteLine("Error occurred while saving image to photo library: {0}", error);
                                }

                                // Delete the temporary file.
                                NSError deleteError;
                                NSFileManager.DefaultManager.Remove(temporaryFileUrl, out deleteError);
                            });
                        }
                    }
                });
            }
        }
    }

    public class CameraVideoTransform : AVCaptureVideoDataOutputSampleBufferDelegate
    {
        private UIImageView m_imageView = null;

        public CameraVideoTransform(UIImageView imageView)
        {
            m_imageView = imageView;
        }

        //
        public override void DidOutputSampleBuffer(AVCaptureOutput captureOutput, CMSampleBuffer sampleBuffer, AVCaptureConnection connection)
        {   //���Դӻ����������һ֡ͼ��ʱ��������ø÷���
            try
            {
                //�ӻ���������ȡһ֡ͼ��
                var image = ImageFromSampleBuffer(sampleBuffer);

                /*
                 * 
                 * do some processing
                 *
                 */
                
                //��ʾ��ǰ֡
                m_imageView.BeginInvokeOnMainThread(delegate()
                {
                    m_imageView.Image = image;
                });

                sampleBuffer.Dispose();
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
        }

        UIImage ImageFromSampleBuffer(CMSampleBuffer sampleBuffer)
        {
            UIImage frame = null;

            // Get a CMSampleBuffer's Core Video image buffer for the media data
            using (var pixelBuffer = sampleBuffer.GetImageBuffer() as CVPixelBuffer)
            {
                // Lock the base address of the pixel buffer
                pixelBuffer.Lock(CVPixelBufferLock.None);

                IntPtr baseAddress = pixelBuffer.BaseAddress;
                int nWidth = (int)pixelBuffer.Width;
                int nHeight = (int)pixelBuffer.Height;
                int nBytesPerRow = (int)pixelBuffer.BytesPerRow;

                using (var colorSpace = CGColorSpace.CreateDeviceRGB())
                using (var context = new CGBitmapContext(baseAddress, nWidth, nHeight, 8, nBytesPerRow, colorSpace, CGBitmapFlags.PremultipliedFirst | CGBitmapFlags.ByteOrder32Little))
                using (var cgImage = context.ToImage())
                {
                    frame = UIImage.FromImage(cgImage);

                    // Unlock the pixel buffer
                    pixelBuffer.Unlock(CVPixelBufferLock.None);
                }
            }

            return frame;
        }
    }
}