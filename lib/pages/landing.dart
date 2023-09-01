import 'package:deadline_reminder/pages/widgets.dart';
import 'package:flutter/material.dart';

/*https: //www.youtube.com/watch?v=R_XjwaSOFmg*/
final List<String> items = [
  'Receive push notification about upcoming deadlines',
  'Choose how early you want to be notified',
];

class LandingPage extends StatefulWidget {
  const LandingPage({super.key, required this.title});

  final String title;

  @override
  State<LandingPage> createState() => _LandingPageState();
}

class _LandingPageState extends State<LandingPage> {
  late PageController _pageController;
  int _currentPage = 0;

  @override
  void initState() {
    super.initState();
    _pageController = PageController(initialPage: _currentPage, viewportFraction: 0.8);
  }

  @override
  void dispose() {
    super.dispose();
    _pageController.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Theme.of(context).colorScheme.inversePrimary,
        title: Text(widget.title),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            SizedBox(
              width: double.infinity,
              height: 100,
              child: PageView.builder(
                controller: _pageController,
                onPageChanged: (int page) => setState(() => _currentPage = page),
                itemCount: items.length,
                itemBuilder: (BuildContext context, int index) => carouselView(index),
              ),
            ),
            verticalSpace(15),
            ElevatedButton(onPressed: () => {}, child: const Text('Get Started')),
          ],
        ),
      ),
    );
  }

  Widget carouselView(int index) {
    return Card(
      elevation: 3,
      child: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Center(
            child: Text(
          items[index],
          textAlign: TextAlign.center,
          style: Theme.of(context).textTheme.bodyLarge,
        )),
      ),
    );
  }
}
